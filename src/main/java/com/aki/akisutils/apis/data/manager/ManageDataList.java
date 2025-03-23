package com.aki.akisutils.apis.data.manager;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ManageDataList {
    public EnumDataType type = EnumDataType.INT;
    public Integer i = 0;
    public Double d = 0.0d;
    public Float f = 0.0f;
    public Long l = 0L;
    public Boolean bl = false;
    public Byte b = 0;
    public String str = "";
    public ItemStack sta = ItemStack.EMPTY;
    public NBTTagCompound en = new NBTTagCompound();
    public List<?> li = new ArrayList<>();
    public NonNullList<?> no = NonNullList.create();
    public NBTTagCompound co = new NBTTagCompound();

    public ManageDataList(Object IDFLBSTRSTA, EnumDataType type) {
        this.type = type;
        if(this.type == EnumDataType.INT && IDFLBSTRSTA instanceof Integer) {
            this.i = (Integer) IDFLBSTRSTA;
        } else if(this.type == EnumDataType.DOUBLE && IDFLBSTRSTA instanceof Double) {
            this.d = (Double) IDFLBSTRSTA;
        } else if(this.type == EnumDataType.FLOAT && IDFLBSTRSTA instanceof Float) {
            this.f = (Float) IDFLBSTRSTA;
        } else if(this.type == EnumDataType.LONG && IDFLBSTRSTA instanceof Long) {
            this.l = (Long) IDFLBSTRSTA;
        } else if(this.type == EnumDataType.BOOLEAN && IDFLBSTRSTA instanceof Boolean) {
            this.bl = (Boolean) IDFLBSTRSTA;
        } else if(this.type == EnumDataType.BYTE && IDFLBSTRSTA instanceof Byte) {
            this.b = (Byte) IDFLBSTRSTA;
        } else if(this.type == EnumDataType.STRING && IDFLBSTRSTA instanceof String) {
            this.str = (String) IDFLBSTRSTA;
        } else if(this.type == EnumDataType.STACK && IDFLBSTRSTA instanceof ItemStack) {
            this.sta = (ItemStack) IDFLBSTRSTA;
        } else if(this.type == EnumDataType.ENTITY && IDFLBSTRSTA instanceof Entity) {
            this.en = ((Entity)IDFLBSTRSTA).writeToNBT(new NBTTagCompound());
        } else if(this.type == EnumDataType.ARRAYLIST && IDFLBSTRSTA instanceof List) {
            this.li = (List<?>) IDFLBSTRSTA;
        } else if(this.type == EnumDataType.NONNULLLIST && IDFLBSTRSTA instanceof NonNullList) {
            this.no = (NonNullList<?>) IDFLBSTRSTA;
        } else if(this.type == EnumDataType.COMPOUND && IDFLBSTRSTA instanceof NBTTagCompound) {
            this.co = (NBTTagCompound) IDFLBSTRSTA;
        }
    }

    public Object getObject() {
        if(this.type == EnumDataType.INT) {
            return this.i;
        } else if(this.type == EnumDataType.DOUBLE) {
            return this.d;//this.d = (Double) IDFLBSTRSTA;
        } else if(this.type == EnumDataType.FLOAT) {
            return this.f;//this.f = (Float) IDFLBSTRSTA;
        } else if(this.type == EnumDataType.LONG) {
            return this.l;//this.l = (Long) IDFLBSTRSTA;
        } else if(this.type == EnumDataType.BOOLEAN) {
            return this.bl;//this.b = (Byte) IDFLBSTRSTA;
        } else if(this.type == EnumDataType.BYTE) {
            return this.b;//this.b = (Byte) IDFLBSTRSTA;
        } else if(this.type == EnumDataType.STRING) {
            return this.str;//this.str = (String) IDFLBSTRSTA;
        } else if(this.type == EnumDataType.STACK) {
            return this.sta;//this.sta = (ItemStack) IDFLBSTRSTA;
        } else if(this.type == EnumDataType.ENTITY) {
            return this.en;//this.sta = (ItemStack) IDFLBSTRSTA;
        } else if(this.type == EnumDataType.ARRAYLIST) {
            return this.li;//this.sta = (ItemStack) IDFLBSTRSTA;
        } else if(this.type == EnumDataType.NONNULLLIST) {
            return this.no;//this.sta = (ItemStack) IDFLBSTRSTA;
        } else if(this.type == EnumDataType.COMPOUND) {
            return this.co;//this.sta = (ItemStack) IDFLBSTRSTA;
        }
        return new Object();
    }

    public static void ObjectToBuf(Object o, ByteBuf buf) {
        try {
            EnumDataType type = getTypeByObject(o);
            if (o instanceof Integer) {
                ByteBufUtils.writeUTF8String(buf, type.getName());
                buf.writeInt((Integer)o);
            } else if (o instanceof Double) {
                ByteBufUtils.writeUTF8String(buf, type.getName());
                buf.writeDouble((Double)o);
            } else if (o instanceof Float) {
                ByteBufUtils.writeUTF8String(buf, type.getName());
                buf.writeFloat((Float) o);
            } else if (o instanceof Long) {
                ByteBufUtils.writeUTF8String(buf, type.getName());
                buf.writeLong((Long) o);
            } else if (o instanceof Boolean) {
                ByteBufUtils.writeUTF8String(buf, type.getName());
                buf.writeBoolean((Boolean) o);
            } else if (o instanceof Byte) {
                ByteBufUtils.writeUTF8String(buf, type.getName());
                buf.writeByte((Byte) o);
            } else if (o instanceof String) {
                ByteBufUtils.writeUTF8String(buf, type.getName());
                ByteBufUtils.writeUTF8String(buf, (String) o);
            } else if (o instanceof ItemStack) {
                ByteBufUtils.writeUTF8String(buf, type.getName());
                ByteBufUtils.writeItemStack(buf, (ItemStack) o);
            } else if (o instanceof NBTTagCompound) {
                ByteBufUtils.writeUTF8String(buf, type.getName());
                ByteBufUtils.writeTag(buf, (NBTTagCompound) o);
            } else {
                throw new Exception("ErrorNotDataType: ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Object BufToObject(ByteBuf buf) {
        try {
            EnumDataType type = EnumDataType.byName(ByteBufUtils.readUTF8String(buf));
            if (EnumDataType.INT == type) {
                return buf.readInt();
            } else if (EnumDataType.DOUBLE == type) {
                return buf.readDouble();
            } else if (EnumDataType.FLOAT == type) {
                return buf.readFloat();
            } else if (EnumDataType.LONG == type) {
                return buf.readLong();
            } else if (EnumDataType.BOOLEAN == type) {
                return buf.readBoolean();
            } else if (EnumDataType.BYTE == type) {
                return buf.readByte();
            } else if (EnumDataType.STRING == type) {
                return ByteBufUtils.readUTF8String(buf);
            } else if (EnumDataType.STACK == type) {
                return ByteBufUtils.readItemStack(buf);
            } else if (EnumDataType.COMPOUND == type) {
                return ByteBufUtils.readTag(buf);
            } else {
                throw new Exception("--Error---MCUtils--ManageDataList-BufToObject-Method");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Object();
    }

    public static EnumDataType getTypeByObject(Object o) throws Exception {
        if(o instanceof Integer) {
            return EnumDataType.INT;
        } else if(o instanceof Double) {
            return EnumDataType.DOUBLE;
        } else if(o instanceof Float) {
            return EnumDataType.FLOAT;
        } else if(o instanceof Long) {
            return EnumDataType.LONG;
        } else if(o instanceof Boolean) {
            return EnumDataType.BOOLEAN;
        } else if(o instanceof Byte) {
            return EnumDataType.INT;
        } else if(o instanceof String) {
            return EnumDataType.STRING;
        } else if(o instanceof ItemStack) {
            return EnumDataType.STACK;
        } else if(o instanceof Entity) {
            return EnumDataType.ENTITY;
        } else if(o instanceof NonNullList) {
            return EnumDataType.NONNULLLIST;
        } else if(o instanceof List) {
            return EnumDataType.ARRAYLIST;
        }  else if(o instanceof NBTTagCompound) {
            return EnumDataType.COMPOUND;
        }
        throw new Exception("NotObjectTypeError: " + o);
    }

    public EnumDataType getType() {
        return this.type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManageDataList dataList = (ManageDataList) o;
        return type == dataList.type && Objects.equals(i, dataList.i) && Objects.equals(d, dataList.d) && Objects.equals(f, dataList.f) && Objects.equals(l, dataList.l) && Objects.equals(bl, dataList.bl) && Objects.equals(b, dataList.b) && Objects.equals(str, dataList.str) && Objects.equals(sta, dataList.sta) && Objects.equals(en, dataList.en) && Objects.equals(li, dataList.li) && Objects.equals(no, dataList.no) && Objects.equals(co, dataList.co);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, i, d, f, l, bl, b, str, sta, en, li, no, co);
    }
}
