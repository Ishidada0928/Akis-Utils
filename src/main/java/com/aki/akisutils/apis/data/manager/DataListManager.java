package com.aki.akisutils.apis.data.manager;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class DataListManager {
    public LinkedList<ManageDataList> dataListManagerList = new LinkedList<>();

    public void addData(int indata) {
        WriteToDataList(indata, EnumDataType.INT);
    }

    public void addData(double indata) {
        WriteToDataList(indata, EnumDataType.DOUBLE);
    }

    public void addData(float indata) {
        WriteToDataList(indata, EnumDataType.FLOAT);
    }

    public void addData(long indata) {
        WriteToDataList(indata, EnumDataType.LONG);
    }

    public void addData(boolean indata) {
        WriteToDataList(indata, EnumDataType.BOOLEAN);
    }

    public void addData(byte indata) {
        WriteToDataList(indata, EnumDataType.BYTE);
    }

    public void addData(String string) {
        WriteToDataList(string, EnumDataType.STRING);
    }

    public void addData(ItemStack stack) {
        WriteToDataList(stack, EnumDataType.STACK);
    }

    public void addData(Entity entity) {
        WriteToDataList(entity, EnumDataType.ENTITY);
    }

    public void addData(List<?> list) {
        WriteToDataList(list, EnumDataType.ARRAYLIST);
    }

    public void addData(NonNullList<?> nonlist) {
        WriteToDataList(nonlist, EnumDataType.NONNULLLIST);
    }

    public void addData(NBTTagCompound tag) {
        WriteToDataList(tag, EnumDataType.COMPOUND);
    }



    public void addDataInt(int indata) {
        WriteToDataList(indata, EnumDataType.INT);
    }

    public void addDataDouble(double indata) {
        WriteToDataList(indata, EnumDataType.DOUBLE);
    }

    public void addDataFloat(float indata) {
        WriteToDataList(indata, EnumDataType.FLOAT);
    }

    public void addDataLong(long indata) {
        WriteToDataList(indata, EnumDataType.LONG);
    }

    public void addDataBoolean(boolean indata) {
        WriteToDataList(indata, EnumDataType.BOOLEAN);
    }

    public void addDataByte(byte indata) {
        WriteToDataList(indata, EnumDataType.BYTE);
    }

    public void addDataString(String string) {
        WriteToDataList(string, EnumDataType.STRING);
    }

    public void addDataStack(ItemStack stack) {
        WriteToDataList(stack, EnumDataType.STACK);
    }

    public void addDataEntity(Entity entity) {
        WriteToDataList(entity, EnumDataType.ENTITY);
    }

    public void addDataList(List<?> list) {
        WriteToDataList(list, EnumDataType.ARRAYLIST);
    }

    public void addDataNonList(NonNullList<?> nonlist) {
        WriteToDataList(nonlist, EnumDataType.NONNULLLIST);
    }

    public void addDataTag(NBTTagCompound tag) {
        WriteToDataList(tag, EnumDataType.COMPOUND);
    }



    public int getDataInt() {
        return (Integer)ReadToDataList().getObject();
    }

    public double getDataDouble() {
        return (Double)ReadToDataList().getObject();
    }

    public float getDataFloat() {
        return (Float) ReadToDataList().getObject();
    }

    public long getDataLong() {
        return (Long)ReadToDataList().getObject();
    }

    public boolean getDataBoolean() {
        return (Boolean)ReadToDataList().getObject();
    }

    public byte getDataByte() {
        return (Byte) ReadToDataList().getObject();
    }

    public String getDataString() {
        return (String) ReadToDataList().getObject();
    }

    public ItemStack getDataItemStack() {
        return (ItemStack)ReadToDataList().getObject();
    }

    public Entity getDataEntity(World world) {
        return (Entity) EntityList.createEntityFromNBT((NBTTagCompound) ReadToDataList().getObject(), world);
    }

    public List<?> getDataList() {
        return (List<?>) ReadToDataList().getObject();
    }

    public NonNullList<?> getDataNonList() {
        return (NonNullList<?>)ReadToDataList().getObject();
    }

    public NBTTagCompound getDataTag() {
        return (NBTTagCompound) ReadToDataList().getObject();
    }



    public ManageDataList ReadToDataList() {
        try {
            //System.out.print(" DATALISTMANAGER_datas:" + new ArrayList<>(this.dataListManagerList));
            ManageDataList dataList = dataListManagerList.get(0);
            dataListManagerList.remove(0);
            return dataList;
        } catch (Exception e) {
            System.out.print(e.getLocalizedMessage());
        }
        return new ManageDataList(0, EnumDataType.BYTE);
    }

    public void WriteToDataList(Object o, EnumDataType type) {
        dataListManagerList.addLast(new ManageDataList(o, type));
    }


    public void readData(ByteBuf buf) {
        int size = buf.readInt();
        for(int i = 0; i < size; i++) {
            String s = ByteBufUtils.readUTF8String(buf);
            EnumDataType type = EnumDataType.byName(s);
            Object object = new Object();
            if(type == EnumDataType.INT) {
                object = buf.readInt();
            } else if(type == EnumDataType.DOUBLE) {
                object = buf.readDouble();
            } else if(type == EnumDataType.FLOAT) {
                object = buf.readFloat();
            } else if(type == EnumDataType.LONG) {
                object = buf.readLong();
            } else if(type == EnumDataType.BOOLEAN) {
                object = buf.readBoolean();
            } else if(type == EnumDataType.BYTE) {
                object = buf.readByte();
            } else if(type == EnumDataType.STRING) {
                object = ByteBufUtils.readUTF8String(buf);
            } else if(type == EnumDataType.STACK) {
                object = ByteBufUtils.readItemStack(buf);
            } else if(type == EnumDataType.ENTITY) {
                object = ByteBufUtils.readTag(buf);
            } else if(type == EnumDataType.ARRAYLIST) {
                int size1 = buf.readInt();
                List<Object> list = new ArrayList<>();
                for(int i2 = 0; i2 < size1; i2++) {
                    list.add(ManageDataList.BufToObject(buf));
                }
                object = list;
            } else if(type == EnumDataType.NONNULLLIST) {
                int size1 = buf.readInt();
                NonNullList<Object> list = NonNullList.create();
                for(int i2 = 0; i2 < size1; i2++) {
                    list.add(ManageDataList.BufToObject(buf));
                }
                object = list;
                //object = ByteBufUtils.readTag(buf);
            } else if(type == EnumDataType.COMPOUND) {
                object = ByteBufUtils.readTag(buf);
            }

            dataListManagerList.add(new ManageDataList(object, type));
        }
    }

    public ByteBuf writeData(ByteBuf buf) {
        buf.writeInt(dataListManagerList.size());
        for(ManageDataList dataList : dataListManagerList) {
            ByteBufUtils.writeUTF8String(buf, dataList.getType().getName());
            EnumDataType type = dataList.type;
            if(type == EnumDataType.INT) {
                buf.writeInt(dataList.i);
            } else if(type == EnumDataType.DOUBLE) {
                buf.writeDouble(dataList.d);
            } else if(type == EnumDataType.FLOAT) {
                buf.writeFloat(dataList.f);
            } else if(type == EnumDataType.LONG) {
                buf.writeLong(dataList.l);
            } else if(type == EnumDataType.BOOLEAN) {
                buf.writeBoolean(dataList.bl);
            } else if(type == EnumDataType.BYTE) {
                buf.writeByte(dataList.b);
            } else if(type == EnumDataType.STRING) {
                ByteBufUtils.writeUTF8String(buf, dataList.str);
            } else if(type == EnumDataType.STACK) {
                ByteBufUtils.writeItemStack(buf, dataList.sta);
            } else if(type == EnumDataType.ENTITY) {
                ByteBufUtils.writeTag(buf, dataList.en);
            } else if(type == EnumDataType.ARRAYLIST) {
                buf.writeInt(dataList.li.size());
                for(Object o : dataList.li.toArray()) {
                    ManageDataList.ObjectToBuf(o, buf);
                }
            } else if(type == EnumDataType.NONNULLLIST) {
                buf.writeInt(dataList.no.size());
                for(Object o : dataList.no.toArray()) {
                    ManageDataList.ObjectToBuf(o, buf);
                }
            } else if(type == EnumDataType.COMPOUND) {
                ByteBufUtils.writeTag(buf, dataList.co);
            }
        }
        return buf;
    }
}
