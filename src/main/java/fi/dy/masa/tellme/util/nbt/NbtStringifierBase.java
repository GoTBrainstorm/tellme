package fi.dy.masa.tellme.util.nbt;

import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.ByteTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.DoubleTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.IntArrayTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.LongArrayTag;
import net.minecraft.nbt.LongTag;
import net.minecraft.nbt.ShortTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

public abstract class NbtStringifierBase
{
    @Nullable protected final String baseColor;
    protected final boolean colored;
    protected final boolean useNumberSuffix;

    protected String tagNameQuote = "\"";
    protected String keyColor = ChatFormatting.YELLOW.toString();
    protected String numberColor = ChatFormatting.GOLD.toString();
    protected String numberTypeColor = ChatFormatting.RED.toString();
    protected String stringColor = ChatFormatting.GREEN.toString();

    public NbtStringifierBase(boolean useNumberSuffix)
    {
        this(useNumberSuffix, null);
    }

    public NbtStringifierBase(boolean useNumberSuffix, @Nullable String baseColor)
    {
        this.colored = baseColor != null;
        this.useNumberSuffix = useNumberSuffix;
        this.baseColor = baseColor;
    }

    protected String getFormattedTagName(String name)
    {
        if (name.length() == 0)
        {
            return name;
        }

        StringBuilder sb = new StringBuilder();
        sb.append(this.tagNameQuote);

        if (this.colored)
        {
            sb.append(this.keyColor);
            sb.append(name);
            sb.append(this.baseColor);
        }
        else
        {
            sb.append(name);
        }

        sb.append(this.tagNameQuote);

        return sb.toString();
    }

    @Nullable
    protected String getPrimitiveValue(Tag tag)
    {
        switch (tag.getId())
        {
            case Tag.TAG_BYTE:    return String.valueOf(((ByteTag) tag).getAsByte());
            case Tag.TAG_SHORT:   return String.valueOf(((ShortTag) tag).getAsShort());
            case Tag.TAG_INT:     return String.valueOf(((IntTag) tag).getAsInt());
            case Tag.TAG_LONG:    return String.valueOf(((LongTag) tag).getAsLong());
            case Tag.TAG_FLOAT:   return String.valueOf(((FloatTag) tag).getAsFloat());
            case Tag.TAG_DOUBLE:  return String.valueOf(((DoubleTag) tag).getAsDouble());
            case Tag.TAG_STRING:  return ((StringTag) tag).getAsString();
        }

        return null;
    }

    @Nullable
    protected String getNumberSuffix(int tagId)
    {
        switch (tagId)
        {
            case Tag.TAG_BYTE:    return "b";
            case Tag.TAG_SHORT:   return "s";
            case Tag.TAG_LONG:    return "L";
            case Tag.TAG_FLOAT:   return "f";
            case Tag.TAG_DOUBLE:  return "d";
        }

        return null;
    }

    @Nullable
    protected String getPrimitiveColorCode(int tagId)
    {
        switch (tagId)
        {
            case Tag.TAG_BYTE:
            case Tag.TAG_SHORT:
            case Tag.TAG_INT:
            case Tag.TAG_LONG:
            case Tag.TAG_FLOAT:
            case Tag.TAG_DOUBLE:
                return this.numberColor;

            case Tag.TAG_STRING:
                return this.stringColor;
        }

        return null;
    }

    protected String getFormattedPrimitiveString(Tag tag)
    {
        String valueStr = this.getPrimitiveValue(tag);
        String valueColorStr = this.colored ? this.getPrimitiveColorCode(tag.getId()) : null;
        String numberSuffixStr = this.useNumberSuffix ? this.getNumberSuffix(tag.getId()) : null;
        boolean useQuotes = tag.getId() == Tag.TAG_STRING;

        return this.getFormattedPrimitiveString(valueStr, useQuotes, valueColorStr, numberSuffixStr);
    }

    protected String getFormattedPrimitiveString(String valueStr, boolean useQuotes, @Nullable String valueColorStr, @Nullable String numberSuffixStr)
    {
        StringBuilder sb = new StringBuilder();

        if (valueStr == null)
        {
            return "";
        }

        if (useQuotes)
        {
            sb.append('"');
        }

        if (valueColorStr != null)
        {
            sb.append(valueColorStr);
        }

        sb.append(valueStr);

        if (numberSuffixStr != null)
        {
            if (this.colored)
            {
                sb.append(this.numberTypeColor);
            }

            sb.append(numberSuffixStr);
        }

        if (this.colored)
        {
            sb.append(this.baseColor);
        }

        if (useQuotes)
        {
            sb.append('"');
        }

        return sb.toString();
    }

    protected void appendTag(String tagName, Tag tag)
    {
        switch (tag.getId())
        {
            case Tag.TAG_COMPOUND:
                this.appendCompound(tagName, (CompoundTag) tag);
                break;

            case Tag.TAG_LIST:
                this.appendList(tagName, (ListTag) tag);
                break;

            case Tag.TAG_BYTE_ARRAY:
                this.appendByteArray(tagName, ((ByteArrayTag) tag).getAsByteArray());
                break;

            case Tag.TAG_INT_ARRAY:
                this.appendIntArray(tagName, ((IntArrayTag) tag).getAsIntArray());
                break;

            case Tag.TAG_LONG_ARRAY:
                this.appendLongArray(tagName, ((LongArrayTag) tag).getAsLongArray());
                break;

            default:
                this.appendPrimitive(tagName, tag);
        }
    }

    protected abstract void appendPrimitive(String tagName, Tag tag);
    protected abstract void appendCompound(String tagName, CompoundTag tag);
    protected abstract void appendList(String tagName, ListTag list);
    protected abstract void appendByteArray(String tagName, byte[] arr);
    protected abstract void appendIntArray(String tagName, int[] arr);
    protected abstract void appendLongArray(String tagName, long[] arr);
}
