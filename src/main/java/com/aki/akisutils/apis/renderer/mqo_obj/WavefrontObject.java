package com.aki.akisutils.apis.renderer.mqo_obj;

import com.aki.akisutils.apis.util.math.NumberUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *  Wavefront Object importer
 *  Based heavily off of the specifications found at http://en.wikipedia.org/wiki/Wavefront_.obj_file
 */
public class WavefrontObject implements IModelCustom
{
    private static Pattern vertexPattern = Pattern.compile("(v( (\\-){0,1}\\d+\\.\\d+){3,4} *\\n)|(v( (\\-){0,1}\\d+\\.\\d+){3,4} *$)");
    private static Pattern vertexNormalPattern = Pattern.compile("(vn( (\\-){0,1}\\d+\\.\\d+){3,4} *\\n)|(vn( (\\-){0,1}\\d+\\.\\d+){3,4} *$)");
    private static Pattern textureCoordinatePattern = Pattern.compile("(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *\\n)|(vt( (\\-){0,1}\\d+\\.\\d+){2,3} *$)");
    private static Pattern face_V_VT_VN_Pattern = Pattern.compile("(f( \\d+/\\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+/\\d+){3,4} *$)");
    private static Pattern face_V_VT_Pattern = Pattern.compile("(f( \\d+/\\d+){3,4} *\\n)|(f( \\d+/\\d+){3,4} *$)");
    private static Pattern face_V_VN_Pattern = Pattern.compile("(f( \\d+//\\d+){3,4} *\\n)|(f( \\d+//\\d+){3,4} *$)");
    private static Pattern face_V_Pattern = Pattern.compile("(f( \\d+){3,4} *\\n)|(f( \\d+){3,4} *$)");
    private static Pattern groupObjectPattern = Pattern.compile("([go]( [\\w\\d]+) *\\n)|([go]( [\\w\\d]+) *$)");

    private Matcher vertexMatcher, vertexNormalMatcher, textureCoordinateMatcher;
    private Matcher face_V_VT_VN_Matcher, face_V_VT_Matcher, face_V_VN_Matcher, face_V_Matcher;
    private Matcher groupObjectMatcher;

    public ArrayList<Vertex> vertices = new ArrayList<Vertex>();
    public ArrayList<Vertex> vertexNormals = new ArrayList<Vertex>();
    public ArrayList<TextureCoordinate> textureCoordinates = new ArrayList<TextureCoordinate>();
    public CopyOnWriteArrayList<GroupObject> groupObjects = new CopyOnWriteArrayList<GroupObject>();//ArrayList
    private GroupObject currentGroupObject;
    private String fileName;

    //TODO -injection START
    public static ExecutorService exec = Executors.newWorkStealingPool();
    public WavefrontObject(ResourceLocation resource) throws ModelFormatException
    {
        this.fileName = resource.toString();

       /* if(cfg_multiCoreLoading) {
            exec.execute(() ->{
                try
                {
                    IResource res = Minecraft.getMinecraft().getResourceManager().getResource(resource);

                    loadObjModel(res.getInputStream());
                }
                catch (Exception e)
                {
                    throw new ModelFormatException("IO Exception reading model format", e);
                }
            });
        }else*/
        {
            try
            {
                IResource res = Minecraft.getMinecraft().getResourceManager().getResource(resource);

                loadObjModel(res.getInputStream());
            }
            catch (IOException e)
            {
                throw new ModelFormatException("IO Exception reading model format", e);
            }
        }
        //TODO -injection END
    }
    
    public WavefrontObject(String filename, InputStream inputStream) throws Exception
    {
        this.fileName = filename;
        loadObjModel(inputStream);
    }

    private void loadObjModel(InputStream inputStream) throws ModelFormatException
    {
        BufferedReader reader = null;

        String currentLine = null;
        int lineCount = 0;

        try
        {
            reader = new BufferedReader(new InputStreamReader(inputStream));

            while ((currentLine = reader.readLine()) != null)
            {
                lineCount++;
                currentLine = currentLine.replaceAll("\\s+", " ").trim();

                if (currentLine.startsWith("#") || currentLine.length() == 0)
                {
                    continue;
                }
                else if (currentLine.startsWith("v "))
                {
                    Vertex vertex = parseVertex(currentLine, lineCount);
                    if (vertex != null)
                    {
                        vertices.add(vertex);
                    }
                }
                else if (currentLine.startsWith("vn "))
                {
                    Vertex vertex = parseVertexNormal(currentLine, lineCount);
                    if (vertex != null)
                    {
                        vertexNormals.add(vertex);
                    }
                }
                else if (currentLine.startsWith("vt "))
                {
                    TextureCoordinate textureCoordinate = parseTextureCoordinate(currentLine, lineCount);
                    if (textureCoordinate != null)
                    {
                        textureCoordinates.add(textureCoordinate);
                    }
                }
                else if (currentLine.startsWith("f "))
                {

                    if (currentGroupObject == null)
                    {
                        currentGroupObject = new GroupObject("Default");
                    }

                    Face face = parseFace(currentLine, lineCount);

                    if (face != null)
                    {
                        currentGroupObject.faces.add(face);
                    }
                }
                else if (currentLine.startsWith("g ") | currentLine.startsWith("o "))
                {
                    GroupObject group = parseGroupObject(currentLine, lineCount);

                    if (group != null)
                    {
                        if (currentGroupObject != null)
                        {
                            groupObjects.add(currentGroupObject);
                        }
                    }

                    currentGroupObject = group;
                }
            }

            groupObjects.add(currentGroupObject);
        }
        catch (IOException e)
        {
            throw new ModelFormatException("IO Exception reading model format", e);
        }
        finally
        {
            try
            {
                reader.close();
            }
            catch (IOException e)
            {
                // hush
            }

            try
            {
                inputStream.close();
            }
            catch (IOException e)
            {
                // hush
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderAll()
    {
    	Tessellator2 tessellator = Tessellator2.instance;

        if (currentGroupObject != null)
        {
            tessellator.startDrawing(currentGroupObject.glDrawingMode);
        }
        else
        {
            tessellator.startDrawing(GL11.GL_TRIANGLES);
        }
        tessellateAll(tessellator);

        tessellator.draw();
    }

    @SideOnly(Side.CLIENT)
    public void tessellateAll(Tessellator2 tessellator)
    {
        for (GroupObject groupObject : groupObjects)
        {
            groupObject.render(tessellator);
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderOnly(String... groupNames)
    {
        for (GroupObject groupObject : groupObjects)
        {
            for (String groupName : groupNames)
            {
                if (groupName.equalsIgnoreCase(groupObject.name))
                {
                    groupObject.render();
                }
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void tessellateOnly(Tessellator2 tessellator, String... groupNames) {
        for (GroupObject groupObject : groupObjects)
        {
            for (String groupName : groupNames)
            {
                if (groupName.equalsIgnoreCase(groupObject.name))
                {
                    groupObject.render(tessellator);
                }
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderPart(String partName)
    {
        for (GroupObject groupObject : groupObjects)
        {
            if (partName.equalsIgnoreCase(groupObject.name))
            {
                groupObject.render();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void tessellatePart(Tessellator2 tessellator, String partName) {
        for (GroupObject groupObject : groupObjects)
        {
            if (partName.equalsIgnoreCase(groupObject.name))
            {
                groupObject.render(tessellator);
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void renderAllExcept(String... excludedGroupNames)
    {
        for (GroupObject groupObject : groupObjects)
        {
            boolean skipPart=false;
            for (String excludedGroupName : excludedGroupNames)
            {
                if (excludedGroupName.equalsIgnoreCase(groupObject.name))
                {
                    skipPart=true;
                }
            }
            if(!skipPart)
            {
                groupObject.render();
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void tessellateAllExcept(Tessellator2 tessellator, String... excludedGroupNames)
    {
        boolean exclude;
        for (GroupObject groupObject : groupObjects)
        {
            exclude=false;
            for (String excludedGroupName : excludedGroupNames)
            {
                if (excludedGroupName.equalsIgnoreCase(groupObject.name))
                {
                    exclude=true;
                }
            }
            if(!exclude)
            {
                groupObject.render(tessellator);
            }
        }
    }

    private Vertex parseVertex(String line, int lineCount) throws ModelFormatException {
        line = CheckStringValues2(line);
        if (isValidVertexLine(line)) {
            line = line.substring(line.indexOf(" ") + 1);
            String[] tokens = line.split(" ");
            try {
                if (tokens.length == 2)
                    return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]), 0.0F);
                if (tokens.length == 3)
                    return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]));
            } catch (NumberFormatException e) {
                throw new ModelFormatException(String.format("Number formatting error at line %d", new Object[] { Integer.valueOf(lineCount) }), e);
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
        }
        return null;
    }

    private Vertex parseVertexNormal(String line, int lineCount) throws ModelFormatException {
        line = CheckStringValues2(line);
        if (isValidVertexNormalLine(line)) {
            line = line.substring(line.indexOf(" ") + 1);
            String[] tokens = line.split(" ");
            try {
                if (tokens.length == 3)
                    return new Vertex(Float.parseFloat(tokens[0]), Float.parseFloat(tokens[1]),
                            Float.parseFloat(tokens[2]));
            } catch (NumberFormatException e) {
                throw new ModelFormatException(String.format("Number formatting error at line %d", new Object[] { Integer.valueOf(lineCount) }), e);
            }
        } else {
            throw new ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
        }
        return null;
    }

    private TextureCoordinate parseTextureCoordinate(String line, int lineCount) throws ModelFormatException {
        /**
         * どうやら、vt 1 0などの整数だけだとエラーになってしまうらしい。
         * だから、整数の後ろに[.000000]などをつけてエラーにならないようにする
         * */
        line = CheckStringValues1(line);
        if (isValidTextureCoordinateLine(line)) {
            line = line.substring(line.indexOf(" ") + 1);
            String[] tokens = line.split(" ");
            try {
                return new TextureCoordinate(Float.parseFloat(tokens[0]), 1.0F - Float.parseFloat(tokens[1]));
            } catch (NumberFormatException e) {
                throw new ModelFormatException(String.format("Number formatting error at line %d", new Object[] { Integer.valueOf(lineCount) }), e);
            }
        }
        throw new ModelFormatException("Error parsing entry ('" + line + "', line " + lineCount + ") in file '" + this.fileName + "' - Incorrect format");
    }

    private Face parseFace(String line, int lineCount) throws ModelFormatException
    {
        Face face = null;

        if (isValidFaceLine(line))
        {
            face = new Face();

            String trimmedLine = line.substring(line.indexOf(" ") + 1);
            String[] tokens = trimmedLine.split(" ");
            String[] subTokens = null;

            if (tokens.length == 3)
            {
                if (currentGroupObject.glDrawingMode == -1)
                {
                    currentGroupObject.glDrawingMode = GL11.GL_TRIANGLES;
                }
                else if (currentGroupObject.glDrawingMode != GL11.GL_TRIANGLES)
                {
                    throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName + "' - Invalid number of points for face (expected 4, found " + tokens.length + ")");
                }
            }
            else if (tokens.length == 4)
            {
                if (currentGroupObject.glDrawingMode == -1)
                {
                    currentGroupObject.glDrawingMode = GL11.GL_QUADS;
                }
                else if (currentGroupObject.glDrawingMode != GL11.GL_QUADS)
                {
                    throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName + "' - Invalid number of points for face (expected 3, found " + tokens.length + ")");
                }
            }

            // f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3 ...
            if (isValidFace_V_VT_VN_Line(line))
            {
                face.vertices = new Vertex[tokens.length];
                face.textureCoordinates = new TextureCoordinate[tokens.length];
                face.vertexNormals = new Vertex[tokens.length];

                for (int i = 0; i < tokens.length; ++i)
                {
                    subTokens = tokens[i].split("/");

                    face.vertices[i] = vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    face.textureCoordinates[i] = textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
                    face.vertexNormals[i] = vertexNormals.get(Integer.parseInt(subTokens[2]) - 1);
                }

                face.faceNormal = face.calculateFaceNormal();
            }
            // f v1/vt1 v2/vt2 v3/vt3 ...
            else if (isValidFace_V_VT_Line(line))
            {
                face.vertices = new Vertex[tokens.length];
                face.textureCoordinates = new TextureCoordinate[tokens.length];

                for (int i = 0; i < tokens.length; ++i)
                {
                    subTokens = tokens[i].split("/");

                    face.vertices[i] = vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    face.textureCoordinates[i] = textureCoordinates.get(Integer.parseInt(subTokens[1]) - 1);
                }

                face.faceNormal = face.calculateFaceNormal();
            }
            // f v1//vn1 v2//vn2 v3//vn3 ...
            else if (isValidFace_V_VN_Line(line))
            {
                face.vertices = new Vertex[tokens.length];
                face.vertexNormals = new Vertex[tokens.length];

                for (int i = 0; i < tokens.length; ++i)
                {
                    subTokens = tokens[i].split("//");

                    face.vertices[i] = vertices.get(Integer.parseInt(subTokens[0]) - 1);
                    face.vertexNormals[i] = vertexNormals.get(Integer.parseInt(subTokens[1]) - 1);
                }

                face.faceNormal = face.calculateFaceNormal();
            }
            // f v1 v2 v3 ...
            else if (isValidFace_V_Line(line))
            {
                face.vertices = new Vertex[tokens.length];

                for (int i = 0; i < tokens.length; ++i)
                {
                    face.vertices[i] = vertices.get(Integer.parseInt(tokens[i]) - 1);
                }

                face.faceNormal = face.calculateFaceNormal();
            }
            else
            {
                throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName + "' - Incorrect format");
            }
        }
        else
        {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName + "' - Incorrect format");
        }

        return face;
    }

    private GroupObject parseGroupObject(String line, int lineCount) throws ModelFormatException
    {
        GroupObject group = null;

        if (isValidGroupObjectLine(line))
        {
            String trimmedLine = line.substring(line.indexOf(" ") + 1);

            if (trimmedLine.length() > 0)
            {
                group = new GroupObject(trimmedLine);
            }
        }
        else
        {
            throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName + "' - Incorrect format");
        }

        return group;
    }

    /***
     * Verifies that the given line from the model file is a valid vertex
     * @param line the line being validated
     * @return true if the line is a valid vertex, false otherwise
     */
    private boolean isValidVertexLine(String line)
    {
        if (vertexMatcher != null)
        {
            vertexMatcher.reset();
        }

        vertexMatcher = vertexPattern.matcher(line);
        return vertexMatcher.matches();
    }

    /***
     * Verifies that the given line from the model file is a valid vertex normal
     * @param line the line being validated
     * @return true if the line is a valid vertex normal, false otherwise
     */
    private boolean isValidVertexNormalLine(String line)
    {
        if (vertexNormalMatcher != null)
        {
            vertexNormalMatcher.reset();
        }

        vertexNormalMatcher = vertexNormalPattern.matcher(line);
        return vertexNormalMatcher.matches();
    }

    /***
     * Verifies that the given line from the model file is a valid texture coordinate
     * @param line the line being validated
     * @return true if the line is a valid texture coordinate, false otherwise
     */
    private boolean isValidTextureCoordinateLine(String line)
    {
        if (textureCoordinateMatcher != null)
        {
            textureCoordinateMatcher.reset();
        }

        textureCoordinateMatcher = textureCoordinatePattern.matcher(line);
        return textureCoordinateMatcher.matches();
    }

    public static String CheckStringValues1(String line) {
        //[0] = vt [1][2] = 1.000000, 0.000000
        String[] strings = line.split(" ");
        if(isNumber(strings[1])) {
            strings[1] = getLiteralCheckAndConvert(!strings[1].contains(".") ? strings[1] + ".000000" : strings[1]);
        }
        if(isNumber(strings[2])) {
            strings[2] = getLiteralCheckAndConvert(!strings[2].contains(".") ? strings[2] + ".000000" : strings[2]);
        }
        return strings[0] + " " + strings[1] + " " + strings[2];
    }

    public static String CheckStringValues2(String line) {
        //[0] = vt [1][2] = 1.000000, 0.000000
        String[] strings = line.split(" ");
        if(isNumber(strings[1])) {
            strings[1] = getLiteralCheckAndConvert(!strings[1].contains(".") ? strings[1] + ".000000" : strings[1]);
        }
        if(isNumber(strings[2])) {
            strings[2] = getLiteralCheckAndConvert(!strings[2].contains(".") ? strings[2] + ".000000" : strings[2]);
        }
        if(isNumber(strings[3])) {
            strings[3] = getLiteralCheckAndConvert(!strings[3].contains(".") ? strings[3] + ".000000" : strings[3]);
        }
        return strings[0] + " " + strings[1] + " " + strings[2] + " " + strings[3];
    }

    public static String getLiteralCheckAndConvert(String search) {
        search = search.replace("E", "e");
        if(search.contains("e")) {
            String[] s1 = search.split("e");
            s1[0] = CheckP(s1[0]);
            //System.out.print("--Check--");
            int Value = Integer.parseInt(s1[1].replace(".000000", ""));
            //System.out.print("Value: " + Value);
            if(Math.abs(Value) <= 3) {
                search = String.valueOf(NumberUtils.getStringLiteralToDouble(search)) + ".000000";
            } else {
                search = "0.000000";
            }
        } else {
            search = CheckP(search);
        }
        return search;
    }

    public static String CheckP(String s) {
        int size = (int)s.chars()
                .filter(c -> c == ".".toCharArray()[0])
                .count();

        if(size >= 2) {
            String[] s1 = s.split("\\.");
            String s2 = "";
            for(int i = 1; i < s1.length; i++)
                s2 += s1[i];
            s = s1[0] + "." + s2;
        }

        return s;
    }

    /**
     * 数値（整数）チェック
     * @param value 検証対象の値
     * @return 結果（true：数値、false：数値ではない）
     */
    public static boolean isNumber(String value) {
        boolean result = false;
        if (value != null) {
            Pattern pattern = Pattern.compile("^([1-9]\\d*|0)(\\.\\d+)?$|^(-[1-9]\\d*|0)(\\.\\d+)?$|^(([1-9]\\d*|0)(\\.\\d+)?|(-[1-9]\\d*|0)(\\.\\d+)?)e([0-9]|-[0-9])+$|^(([1-9]\\d*|0)(\\.\\d+)?|(-[1-9]\\d*|0)(\\.\\d+)?)e([+]+[0-9]|-[0-9])+$|^(([1-9]\\d*|0)(\\.\\d+)?|(-[1-9]\\d*|0)(\\.\\d+)?)E([0-9]|-[0-9])+$|^(([1-9]\\d*|0)(\\.\\d+)?|(-[1-9]\\d*|0)(\\.\\d+)?)E([+]+[0-9]|-[0-9])+$");
            result = pattern.matcher(value).matches();
        }
        return result;
    }

    /***
     * Verifies that the given line from the model file is a valid face that is described by vertices, texture coordinates, and vertex normals
     * @param line the line being validated
     * @return true if the line is a valid face that matches the format "f v1/vt1/vn1 ..." (with a minimum of 3 points in the face, and a maximum of 4), false otherwise
     */
    private boolean isValidFace_V_VT_VN_Line(String line)
    {
        if (face_V_VT_VN_Matcher != null)
        {
            face_V_VT_VN_Matcher.reset();
        }

        face_V_VT_VN_Matcher = face_V_VT_VN_Pattern.matcher(line);
        return face_V_VT_VN_Matcher.matches();
    }

    /***
     * Verifies that the given line from the model file is a valid face that is described by vertices and texture coordinates
     * @param line the line being validated
     * @return true if the line is a valid face that matches the format "f v1/vt1 ..." (with a minimum of 3 points in the face, and a maximum of 4), false otherwise
     */
    private boolean isValidFace_V_VT_Line(String line)
    {
        if (face_V_VT_Matcher != null)
        {
            face_V_VT_Matcher.reset();
        }

        face_V_VT_Matcher = face_V_VT_Pattern.matcher(line);
        return face_V_VT_Matcher.matches();
    }

    /***
     * Verifies that the given line from the model file is a valid face that is described by vertices and vertex normals
     * @param line the line being validated
     * @return true if the line is a valid face that matches the format "f v1//vn1 ..." (with a minimum of 3 points in the face, and a maximum of 4), false otherwise
     */
    private boolean isValidFace_V_VN_Line(String line)
    {
        if (face_V_VN_Matcher != null)
        {
            face_V_VN_Matcher.reset();
        }

        face_V_VN_Matcher = face_V_VN_Pattern.matcher(line);
        return face_V_VN_Matcher.matches();
    }

    /***
     * Verifies that the given line from the model file is a valid face that is described by only vertices
     * @param line the line being validated
     * @return true if the line is a valid face that matches the format "f v1 ..." (with a minimum of 3 points in the face, and a maximum of 4), false otherwise
     */
    private boolean isValidFace_V_Line(String line)
    {
        if (face_V_Matcher != null)
        {
            face_V_Matcher.reset();
        }

        face_V_Matcher = face_V_Pattern.matcher(line);
        return face_V_Matcher.matches();
    }

    /***
     * Verifies that the given line from the model file is a valid face of any of the possible face formats
     * @param line the line being validated
     * @return true if the line is a valid face that matches any of the valid face formats, false otherwise
     */
    private boolean isValidFaceLine(String line)
    {
        return isValidFace_V_VT_VN_Line(line) || isValidFace_V_VT_Line(line) || isValidFace_V_VN_Line(line) || isValidFace_V_Line(line);
    }

    /***
     * Verifies that the given line from the model file is a valid group (or object)
     * @param line the line being validated
     * @return true if the line is a valid group (or object), false otherwise
     */
    private boolean isValidGroupObjectLine(String line)
    {
        if (groupObjectMatcher != null)
        {
            groupObjectMatcher.reset();
        }

        groupObjectMatcher = groupObjectPattern.matcher(line);
        return groupObjectMatcher.matches();
    }

    @Override
    public String getType()
    {
        return "obj";
    }
}
