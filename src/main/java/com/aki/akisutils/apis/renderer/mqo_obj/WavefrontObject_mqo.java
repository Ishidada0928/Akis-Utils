package com.aki.akisutils.apis.renderer.mqo_obj;

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

@SideOnly(Side.CLIENT)
public class WavefrontObject_mqo implements IModelCustom
{
	public ArrayList<Vertex_mqo>		vertices		= new ArrayList<Vertex_mqo>();
	public CopyOnWriteArrayList <GroupObject_mqo>	groupObjects	= new CopyOnWriteArrayList <GroupObject_mqo>();//ArrayList 
	private GroupObject_mqo				currentGroupObject	= null;
	private String						fileName;
	private int							vertexNum = 0;
	private int							faceNum = 0;

	public float	min  =  1000000;
	public float	minX =  1000000;
	public float	minY =  1000000;
	public float	minZ =  1000000;

	public float	max  = -1000000;
	public float	maxX = -1000000;
	public float	maxY = -1000000;
	public float	maxZ = -1000000;

	public float	size  = 0;
	public float	sizeX = 0;
	public float	sizeY = 0;
	public float	sizeZ = 0;

	public WavefrontObject_mqo(ResourceLocation resource) throws ModelFormatException
	{
		this.fileName = resource.toString();
		//TODO -injection START
		if(Runtime.getRuntime().availableProcessors() >= 2) {
			WavefrontObject.exec.execute(() ->{
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
		} else
		{
			try
			{
				IResource res = Minecraft.getMinecraft().getResourceManager().getResource(resource);

				loadObjModel(res.getInputStream());
				//new_loadObjModel(resource.);
			}
			catch (IOException e)
			{
				throw new ModelFormatException("IO Exception reading model format", e);
			}
		}
		//TODO -injection END
	}

	@Override
	public String getType()
	{
		return "mqo";
	}

	public int getVertexNum()
	{
		return this.vertexNum;
	}

	public int getFaceNum()
	{
		return this.faceNum;
	}

	public String getFileName()
	{
		return this.fileName;
	}

	public void checkMinMax(Vertex_mqo v)
	{
		if(v.x < this.minX) this.minX = v.x;
		if(v.y < this.minY) this.minY = v.y;
		if(v.z < this.minZ) this.minZ = v.z;
		if(v.x > this.maxX) this.maxX = v.x;
		if(v.y > this.maxY) this.maxY = v.y;
		if(v.z > this.maxZ) this.maxZ = v.z;
	}

	public boolean containsPart(String partName)
	{
		for(GroupObject_mqo groupObject : groupObjects)
		{
			if (partName.equalsIgnoreCase(groupObject.name))
			{
				return true;
			}
		}
		return false;
	}

	@Override
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

		for (GroupObject_mqo groupObject : groupObjects)
		{
			groupObject.render(tessellator);
		}

		tessellator.draw();
	}

	@Override
	public void renderOnly(String... groupNames)
	{
		for (GroupObject_mqo groupObject : groupObjects)
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

	@Override
	public void renderPart(String partName)
	{
		if(partName.charAt(0)=='$')
		{
			for (int i=0; i < groupObjects.size(); i++)
			{
				GroupObject_mqo groupObject = groupObjects.get(i);
				if (partName.equalsIgnoreCase(groupObject.name))
				{
					groupObject.render();

					i++;
					for (; i < groupObjects.size(); i++)
					{
						groupObject = groupObjects.get(i);
						if(groupObject.name.charAt(0)=='$')
						{
							break;
						}
						groupObject.render();
					}
				}
			}
		}
		else
		{
			for (GroupObject_mqo groupObject : groupObjects)
			{
				if (partName.equalsIgnoreCase(groupObject.name))
				{
					groupObject.render();
				}
			}
		}
	}

	@Override
	public void renderAllExcept(String... excludedGroupNames)
	{
		for (GroupObject_mqo groupObject : groupObjects)
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


	public void renderAllLine(int startLine, int maxLine)
	{
		Tessellator2 tessellator = Tessellator2.instance;

		tessellator.startDrawing(1);

		renderAllLine(tessellator, startLine, maxLine);

		tessellator.draw();
	}

	public void renderAllLine(Tessellator2 tessellator, int startLine, int maxLine)
	{
		int lineCnt = 0;
		for (GroupObject_mqo groupObject : groupObjects)
		{
			if (groupObject.faces.size() > 0)
			{
				for (Face_mqo face : groupObject.faces)
				{
					for (int i = 0; i < face.vertices.length/3; ++i)
					{
						Vertex_mqo v1 = face.vertices[i*3 + 0];
						Vertex_mqo v2 = face.vertices[i*3 + 1];
						Vertex_mqo v3 = face.vertices[i*3 + 2];

						lineCnt++;
						if(lineCnt > maxLine) return;
						tessellator.addVertex(v1.x, v1.y, v1.z);
						tessellator.addVertex(v2.x, v2.y, v2.z);

						lineCnt++;
						if(lineCnt > maxLine) return;
						tessellator.addVertex(v2.x, v2.y, v2.z);
						tessellator.addVertex(v3.x, v3.y, v3.z);

						lineCnt++;
						if(lineCnt > maxLine) return;
						tessellator.addVertex(v3.x, v3.y, v3.z);
						tessellator.addVertex(v1.x, v1.y, v1.z);
					}
				}
			}
		}
	}

	public void renderAll(int startFace, int maxFace)
	{
		if(startFace < 0) startFace = 0;

		Tessellator2 tessellator = Tessellator2.instance;

		tessellator.startDrawing(GL11.GL_TRIANGLES);

		renderAll(tessellator, startFace, maxFace);

		tessellator.draw();
	}

	public void renderAll(Tessellator2 tessellator, int startFace, int maxLine)
	{
		int faceCnt = 0;
		for (GroupObject_mqo groupObject : groupObjects)
		{
			if (groupObject.faces.size() > 0)
			{
				for (Face_mqo face : groupObject.faces)
				{
					faceCnt++;
					if(faceCnt < startFace) continue;
					if(faceCnt > maxLine) return;
					face.addFaceForRender(tessellator);
				}
			}
		}
	}

/*
	private void new_loadObjModel(final String path) throws ModelFormatException
	{
		String file = null;
		String currentLine = null;
		int lineCount = 0;
		try {
			file = Files.lines(Paths.get(path), Charset.forName("UTF-8")).collect(Collectors.joining(System.getProperty("line.separator")));
			//file = Files.lines(Paths.get(path), Charset.forName("UTF-8")).collect(Collectors.joining(System.getProperty("line.separator")));
			
			while ((currentLine = file) != null)
			{
				lineCount++;
				currentLine = currentLine.replaceAll("\\s+", " ").trim();

				// オブジェクトを探す
				if(isValidGroupObjectLine(currentLine))
				{
					GroupObject_mqo group = parseGroupObject(currentLine, lineCount);
					if(group == null)
					{
						continue;
					}

					group.glDrawingMode = GL11.GL_TRIANGLES;

					this.vertices.clear();
					int vertexNum = 0;

					boolean mirror = false;

					double  facet   = Math.cos(45 * 3.1415926535 / 180.0);
					boolean shading = false;

					// シェーディングの設定と頂点数読み込み
					while ((currentLine = file) != null)
					{
						lineCount++;
						currentLine = currentLine.replaceAll("\\s+", " ").trim();

						if(currentLine.equalsIgnoreCase("mirror 1"))
						{
							mirror = true;
						}
						if(currentLine.equalsIgnoreCase("shading 1"))
						{
							shading = true;
						}

						String s[] = currentLine.split(" ");
						if(s.length==2 && s[0].equalsIgnoreCase("facet"))
						{
							facet   = Math.cos(Double.parseDouble(s[1]) * 3.1415926535 / 180.0);
						}

						if(isValidVertexLine(currentLine))
						{
							vertexNum = Integer.valueOf(currentLine.split(" ")[1]);
							break;
						}
					}

					// 頂点読み込み
					if(vertexNum > 0)
					{
						while ((currentLine = file) != null)
						{
							lineCount++;
							currentLine = currentLine.replaceAll("\\s+", " ").trim();

							String s[] = currentLine.split(" ");
							if(s.length == 3)
							{
								Vertex_mqo v = new Vertex_mqo(
										Float.valueOf(s[0]) / 100,
										Float.valueOf(s[1]) / 100,
										Float.valueOf(s[2]) / 100);

								if(v.x < this.minX) this.minX = v.x;
								if(v.y < this.minY) this.minY = v.y;
								if(v.z < this.minZ) this.minZ = v.z;
								if(v.x > this.maxX) this.maxX = v.x;
								if(v.y > this.maxY) this.maxY = v.y;
								if(v.z > this.maxZ) this.maxZ = v.z;

								this.vertices.add(v);

								vertexNum--;

								if(vertexNum <= 0)
								{
									break;
								}
							}
							else if(s.length > 0)
							{
								throw new ModelFormatException("format error : "+this.fileName+" : line="+lineCount);
							}
						}

						int faceNum = 0;
						// 面数読み込み
						while ((currentLine = file) != null)
						{
							lineCount++;
							currentLine = currentLine.replaceAll("\\s+", " ").trim();

							if(isValidFaceLine(currentLine))
							{
								faceNum = Integer.valueOf(currentLine.split(" ")[1]);
								break;
							}
						}

						if(faceNum > 0)
						{
							while ((currentLine = file) != null)
							{
								lineCount++;
								currentLine = currentLine.replaceAll("\\s+", " ").trim();

								String s[] = currentLine.split(" ");
								if(s.length > 2)
								{
									if(Integer.valueOf(s[0]) >= 3)
									{
										Face_mqo faces[] = parseFace(currentLine, lineCount, mirror);
										for(Face_mqo face : faces)
										{
											group.faces.add(face);
										}
									}
									faceNum--;
									if(faceNum <= 0)
									{
										break;
									}
								}
								else if(s.length > 2 && Integer.valueOf(s[0])!=3)
								{
									throw new ModelFormatException("found face is not triangle : "+this.fileName+" : line="+lineCount);
								}
							}

							calcVerticesNormal(group, shading, facet);
						}
					}
					this.vertexNum += this.vertices.size();
					this.faceNum   += group.faces.size();
					this.vertices.clear();

					groupObjects.add(group);
				}
			}
		
		} catch (IOException e) {
			// TODO 自動生成された catch ブロック
			throw new ModelFormatException("IO Exception reading model format : "+this.fileName, e);
		}
		finally
		{
			if(this.minX < this.min)	this.min = this.minX;
			if(this.minY < this.min)	this.min = this.minY;
			if(this.minZ < this.min)	this.min = this.minZ;
			if(this.maxX > this.max)	this.max = this.maxX;
			if(this.maxY > this.max)	this.max = this.maxY;
			if(this.maxZ > this.max)	this.max = this.maxZ;
			this.sizeX = this.maxX - this.minX;
			this.sizeY = this.maxY - this.minY;
			this.sizeZ = this.maxZ - this.minZ;
			this.size  = this.max  - this.min;

			this.vertices = null;
			/*try
			{
				reader.close();
			}
			catch (IOException e)
			{
				// hush
			}

			/*try
			{
				inputStream.close();
			}
			catch (IOException e)
			{
				// hush
			}
		}
	}*/


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

				// オブジェクトを探す
				if(isValidGroupObjectLine(currentLine))
				{
					GroupObject_mqo group = parseGroupObject(currentLine, lineCount);
					if(group == null)
					{
						continue;
					}

					group.glDrawingMode = GL11.GL_TRIANGLES;

					this.vertices.clear();
					int vertexNum = 0;

					boolean mirror = false;

					double  facet   = Math.cos(45 * 3.1415926535 / 180.0);
					boolean shading = false;

					// シェーディングの設定と頂点数読み込み
					while ((currentLine = reader.readLine()) != null)
					{
						lineCount++;
						currentLine = currentLine.replaceAll("\\s+", " ").trim();

						if(currentLine.equalsIgnoreCase("mirror 1"))
						{
							mirror = true;
						}
						if(currentLine.equalsIgnoreCase("shading 1"))
						{
							shading = true;
						}

						String s[] = currentLine.split(" ");
						if(s.length==2 && s[0].equalsIgnoreCase("facet"))
						{
							facet   = Math.cos(Double.parseDouble(s[1]) * 3.1415926535 / 180.0);
						}

						if(isValidVertexLine(currentLine))
						{
							vertexNum = Integer.valueOf(currentLine.split(" ")[1]);
							break;
						}
					}

					// 頂点読み込み
					if(vertexNum > 0)
					{
						while ((currentLine = reader.readLine()) != null)
						{
							lineCount++;
							currentLine = currentLine.replaceAll("\\s+", " ").trim();

							String s[] = currentLine.split(" ");
							if(s.length == 3)
							{
								Vertex_mqo v = new Vertex_mqo(
										Float.valueOf(s[0]) / 100,
										Float.valueOf(s[1]) / 100,
										Float.valueOf(s[2]) / 100);

								if(v.x < this.minX) this.minX = v.x;
								if(v.y < this.minY) this.minY = v.y;
								if(v.z < this.minZ) this.minZ = v.z;
								if(v.x > this.maxX) this.maxX = v.x;
								if(v.y > this.maxY) this.maxY = v.y;
								if(v.z > this.maxZ) this.maxZ = v.z;

								this.vertices.add(v);

								vertexNum--;

								if(vertexNum <= 0)
								{
									break;
								}
							}
							else if(s.length > 0)
							{
								throw new ModelFormatException("format error : "+this.fileName+" : line="+lineCount);
							}
						}

						int faceNum = 0;
						// 面数読み込み
						while ((currentLine = reader.readLine()) != null)
						{
							lineCount++;
							currentLine = currentLine.replaceAll("\\s+", " ").trim();

							if(isValidFaceLine(currentLine))
							{
								faceNum = Integer.valueOf(currentLine.split(" ")[1]);
								break;
							}
						}

						if(faceNum > 0)
						{
							while ((currentLine = reader.readLine()) != null)
							{
								lineCount++;
								currentLine = currentLine.replaceAll("\\s+", " ").trim();

								String s[] = currentLine.split(" ");
								if(s.length > 2)
								{
									if(Integer.valueOf(s[0]) >= 3)
									{
										Face_mqo faces[] = parseFace(currentLine, lineCount, mirror);
										for(Face_mqo face : faces)
										{
											group.faces.add(face);
										}
									}
									faceNum--;
									if(faceNum <= 0)
									{
										break;
									}
								}
								else if(s.length > 2 && Integer.valueOf(s[0])!=3)
								{
									throw new ModelFormatException("found face is not triangle : "+this.fileName+" : line="+lineCount);
								}
							}

							calcVerticesNormal(group, shading, facet);
						}
					}
					this.vertexNum += this.vertices.size();
					this.faceNum   += group.faces.size();
					this.vertices.clear();

					groupObjects.add(group);
				}
			}
		}
		catch (IOException e)
		{
			throw new ModelFormatException("IO Exception reading model format : "+this.fileName, e);
		}
		finally
		{
			if(this.minX < this.min)	this.min = this.minX;
			if(this.minY < this.min)	this.min = this.minY;
			if(this.minZ < this.min)	this.min = this.minZ;
			if(this.maxX > this.max)	this.max = this.maxX;
			if(this.maxY > this.max)	this.max = this.maxY;
			if(this.maxZ > this.max)	this.max = this.maxZ;
			this.sizeX = this.maxX - this.minX;
			this.sizeY = this.maxY - this.minY;
			this.sizeZ = this.maxZ - this.minZ;
			this.size  = this.max  - this.min;

			this.vertices = null;
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


	private void calcVerticesNormal(GroupObject_mqo group, boolean shading, double facet)
	{
		for(Face_mqo f : group.faces)
		{
			f.vertexNormals = new Vertex_mqo[f.verticesID.length];
			for(int i=0; i<f.verticesID.length; i++)
			{
				Vertex_mqo vn = getVerticesNormalFromFace(f.faceNormal, f.verticesID[i], group, (float)facet);
				vn.normalize();

// http://sky.geocities.jp/freakish_osprey/opengl/opengl_normalvecotr.htm
// http://ft-lab.ne.jp/cgi-bin/wiki.cgi?page=%CC%CC%CB%A1%C0%FE%A4%C8%C4%BA%C5%C0%CB%A1%C0%FE_3DCG
				// スムージングの角度を最大45°としたときの面の表面のとある座標位置での面法線をN、頂点法線をVNとして考えてみます。
				// 「c1 >= c2」のときは頂点法線を採用します。「c1 < c2」のときは面法線を採用します。
				// スムージング角度が大きいほどスムーズになり、スムージング角度が小さいとフラットなシェーディングになります。
//				double c1 = f.faceNormal.x * vn.x + f.faceNormal.y * vn.y + f.faceNormal.z * vn.z;

				if(shading)
				{
					if(f.faceNormal.x * vn.x + f.faceNormal.y * vn.y + f.faceNormal.z * vn.z >= facet)
					{
						f.vertexNormals[i] = vn;
					}
					else
					{
						f.vertexNormals[i] = f.faceNormal;
					}
				}
				else
				{
					f.vertexNormals[i] = f.faceNormal;
				}
			}
		}
	}

	private Vertex_mqo getVerticesNormalFromFace(Vertex_mqo faceNormal, int verticesID, GroupObject_mqo group, float facet)
	{
		Vertex_mqo v = new Vertex_mqo(0,0,0);

		for(Face_mqo f : group.faces)
		{
			for(int id : f.verticesID)
			{
				if(id==verticesID)
				{
					if(f.faceNormal.x * faceNormal.x + f.faceNormal.y * faceNormal.y + f.faceNormal.z * faceNormal.z >= facet)
					{
						v.add(f.faceNormal);
					}
					break;
				}
			}
		}

		v.normalize();

		return v;
	}

	private Face_mqo[] parseFace(String line, int lineCount, boolean mirror)
	{
		String s[] = line.split("[ VU)(M]+");
		// Format
		// 3 V(0 2 1) M(0) UV(0.30158 0.75859 0.32219 0.75859 0.28098 0.75859)
		// ↓
		// 3	0 2 1	0	0.30158 0.75859	0.32219 0.75859	0.28098 0.75859

		int vnum = Integer.valueOf(s[0]);
		if(vnum!=3 && vnum!=4)
		{
			return new Face_mqo[]{};
		}

		if(vnum == 3)
		{
			Face_mqo face = new Face_mqo();
			face.verticesID = new int[]
				{
					Integer.valueOf(s[3]),
					Integer.valueOf(s[2]),
					Integer.valueOf(s[1]),
				};

			face.vertices = new Vertex_mqo[]{
					this.vertices.get(face.verticesID[0]),
					this.vertices.get(face.verticesID[1]),
					this.vertices.get(face.verticesID[2]),
			};
			if(s.length>=11)
			{
				face.textureCoordinates = new TextureCoordinate_mqo[]{
					new TextureCoordinate_mqo(Float.valueOf(s[9]), Float.valueOf(s[10])),
					new TextureCoordinate_mqo(Float.valueOf(s[7]), Float.valueOf(s[8])),
					new TextureCoordinate_mqo(Float.valueOf(s[5]), Float.valueOf(s[6])),
				};
			}
			else
			{
				face.textureCoordinates = new TextureCoordinate_mqo[]{
					new TextureCoordinate_mqo(0,0),
					new TextureCoordinate_mqo(0,0),
					new TextureCoordinate_mqo(0,0),
				};
			}
			face.faceNormal = face.calculateFaceNormal();

			return new Face_mqo[]{ face };
		}
		else
		{
			Face_mqo face1 = new Face_mqo();
			face1.verticesID = new int[]
					{
						Integer.valueOf(s[3]),
						Integer.valueOf(s[2]),
						Integer.valueOf(s[1]),
					};

			face1.vertices = new Vertex_mqo[]{
					this.vertices.get(face1.verticesID[0]),
					this.vertices.get(face1.verticesID[1]),
					this.vertices.get(face1.verticesID[2]),
			};

			if(s.length>=12)
			{
				face1.textureCoordinates = new TextureCoordinate_mqo[]{
					new TextureCoordinate_mqo(Float.valueOf(s[10]), Float.valueOf(s[11])),
					new TextureCoordinate_mqo(Float.valueOf(s[ 8]), Float.valueOf(s[ 9])),
					new TextureCoordinate_mqo(Float.valueOf(s[ 6]), Float.valueOf(s[ 7])),
				};
			}
			else
			{
				face1.textureCoordinates = new TextureCoordinate_mqo[]{
					new TextureCoordinate_mqo(0,0),
					new TextureCoordinate_mqo(0,0),
					new TextureCoordinate_mqo(0,0),
				};
			}

			face1.faceNormal = face1.calculateFaceNormal();


			Face_mqo face2 = new Face_mqo();
			face2.verticesID = new int[]
					{
						Integer.valueOf(s[4]),
						Integer.valueOf(s[3]),
						Integer.valueOf(s[1]),
					};

			face2.vertices = new Vertex_mqo[]{
					this.vertices.get(face2.verticesID[0]),
					this.vertices.get(face2.verticesID[1]),
					this.vertices.get(face2.verticesID[2]),
			};

			if(s.length>=14)
			{
				face2.textureCoordinates = new TextureCoordinate_mqo[]{
					new TextureCoordinate_mqo(Float.valueOf(s[12]), Float.valueOf(s[13])),
					new TextureCoordinate_mqo(Float.valueOf(s[10]), Float.valueOf(s[11])),
					new TextureCoordinate_mqo(Float.valueOf(s[ 6]), Float.valueOf(s[ 7])),
				};
			}
			else
			{
				face2.textureCoordinates = new TextureCoordinate_mqo[]{
					new TextureCoordinate_mqo(0,0),
					new TextureCoordinate_mqo(0,0),
					new TextureCoordinate_mqo(0,0),
				};
			}
			face2.faceNormal = face2.calculateFaceNormal();

			return new Face_mqo[]{ face1, face2 };
		}
	}

	// オブジェクトの開始行かどうか判別
	private static boolean isValidGroupObjectLine(String line)
	{
		// Object "obj4" {
		String[] s = line.split(" ");

		if(s.length < 2 || !s[0].equals("Object"))
		{
			return false;
		}

		if(s[1].length()<4 || s[1].charAt(0)!='"')
		{
			return false;
		}

		return true;
	}
	private GroupObject_mqo parseGroupObject(String line, int lineCount) throws ModelFormatException
	{
		GroupObject_mqo group = null;

		if (isValidGroupObjectLine(line))
		{
			String s[] = line.split(" ");
			String trimmedLine = s[1].substring(1, s[1].length()-1);

			if (trimmedLine.length() > 0)
			{
				group = new GroupObject_mqo(trimmedLine);
			}
		}
		else
		{
			throw new ModelFormatException("Error parsing entry ('" + line + "'" + ", line " + lineCount + ") in file '" + fileName + "' - Incorrect format");
		}

		return group;
	}

	// 頂点の開始行かどうか判別
	private static boolean isValidVertexLine(String line)
	{
		String s[] = line.split(" ");

		if(!s[0].equals("vertex")) return false;

		return true;
	}

	/***
	 * Verifies that the given line from the model file is a valid face of any of the possible face formats
	 * @param line the line being validated
	 * @return true if the line is a valid face that matches any of the valid face formats, false otherwise
	 */
	private static boolean isValidFaceLine(String line)
	{
		String s[] = line.split(" ");

		if(!s[0].equals("face")) return false;

		return true;
	}
}
