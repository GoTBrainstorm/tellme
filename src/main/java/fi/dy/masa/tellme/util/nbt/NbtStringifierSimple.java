package fi.dy.masa.tellme.util.nbt;

import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import fi.dy.masa.tellme.util.Constants;

public class NbtStringifierSimple extends NbtStringifierBase
{
    protected StringBuilder stringBuilder;

    public NbtStringifierSimple(@Nullable String baseColor)
    {
        super(true, baseColor);
    }

    public String getNbtString(CompoundTag tag)
    {
        this.stringBuilder = new StringBuilder();

        if (this.colored)
        {
            this.stringBuilder.append(this.baseColor);
        }

        this.appendCompound("", tag);

        return this.stringBuilder.toString();
    }

    @Override
    protected void appendPrimitive(String tagName, Tag tag)
    {
        this.stringBuilder.append(this.getFormattedPrimitiveString(tag));
    }

    @Override
    protected void appendCompound(String tagName, CompoundTag tag)
    {
        List<String> keys = Lists.newArrayList(tag.getKeys());
        Collections.sort(keys);
        boolean first = true;

        this.stringBuilder.append('{');

        for (String key : keys)
        {
            if (first == false)
            {
                this.stringBuilder.append(',');
            }

            this.stringBuilder.append(this.getFormattedTagName(key));
            this.stringBuilder.append(':');
            this.appendTag(key, tag.get(key));
            first = false;
        }

        this.stringBuilder.append('}');
    }

    @Override
    protected void appendList(String tagName, ListTag list)
    {
        final int size = list.size();

        this.stringBuilder.append('[');

        for (int i = 0; i < size; ++i)
        {
            if (i > 0)
            {
                this.stringBuilder.append(',');
            }

            this.appendTag("", list.get(i));
        }

        this.stringBuilder.append(']');
    }

    @Override
    protected void appendByteArray(String tagName, byte[] arr)
    {
        String valueColorStr = this.colored ? this.getPrimitiveColorCode(Constants.NBT.TAG_BYTE) : null;
        String numberSuffixStr = this.useNumberSuffix ? this.getNumberSuffix(Constants.NBT.TAG_BYTE) : null;
        final int size = arr.length;

        this.stringBuilder.append('[');

        for (int i = 0; i < size; ++i)
        {
            if (i > 0)
            {
                this.stringBuilder.append(',');
            }

            this.stringBuilder.append(this.getFormattedPrimitiveString(String.valueOf(arr[i]), false, valueColorStr, numberSuffixStr));
        }

        this.stringBuilder.append(']');
    }

    @Override
    protected void appendIntArray(String tagName, int[] arr)
    {
        String valueColorStr = this.colored ? this.getPrimitiveColorCode(Constants.NBT.TAG_INT) : null;
        String numberSuffixStr = this.useNumberSuffix ? this.getNumberSuffix(Constants.NBT.TAG_INT) : null;
        final int size = arr.length;

        this.stringBuilder.append('[');

        for (int i = 0; i < size; ++i)
        {
            if (i > 0)
            {
                this.stringBuilder.append(',');
            }

            this.stringBuilder.append(this.getFormattedPrimitiveString(String.valueOf(arr[i]), false, valueColorStr, numberSuffixStr));
        }

        this.stringBuilder.append(']');
    }

    @Override
    protected void appendLongArray(String tagName, long[] arr)
    {
        String valueColorStr = this.colored ? this.getPrimitiveColorCode(Constants.NBT.TAG_LONG) : null;
        String numberSuffixStr = this.useNumberSuffix ? this.getNumberSuffix(Constants.NBT.TAG_LONG) : null;
        final int size = arr.length;

        this.stringBuilder.append('[');

        for (int i = 0; i < size; ++i)
        {
            if (i > 0)
            {
                this.stringBuilder.append(',');
            }

            this.stringBuilder.append(this.getFormattedPrimitiveString(String.valueOf(arr[i]), false, valueColorStr, numberSuffixStr));
        }

        this.stringBuilder.append(']');
    }
}
