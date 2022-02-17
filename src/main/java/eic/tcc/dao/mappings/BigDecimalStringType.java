package eic.tcc.dao.mappings;

import java.math.BigDecimal;

import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.descriptor.sql.VarcharTypeDescriptor;

@SuppressWarnings("serial")
public class BigDecimalStringType extends AbstractSingleColumnStandardBasicType<BigDecimal> 
{
    public static final BigDecimalStringType INSTANCE = new BigDecimalStringType();

    public BigDecimalStringType() 
    {
        super(VarcharTypeDescriptor.INSTANCE, BigDecimalStringJavaDescriptor.INSTANCE);
    }

    @Override
    public String getName() {
        return "BigDecimalString";
    }
}