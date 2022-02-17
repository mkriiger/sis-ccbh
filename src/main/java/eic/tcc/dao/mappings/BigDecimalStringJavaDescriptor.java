package eic.tcc.dao.mappings;

import java.math.BigDecimal;

import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractTypeDescriptor;
import org.hibernate.type.descriptor.java.ImmutableMutabilityPlan;

@SuppressWarnings({"serial","unchecked"})
public class BigDecimalStringJavaDescriptor extends AbstractTypeDescriptor<BigDecimal> 
{
    public static final BigDecimalStringJavaDescriptor INSTANCE = new BigDecimalStringJavaDescriptor();

	public BigDecimalStringJavaDescriptor() 
    {
        super(BigDecimal.class, ImmutableMutabilityPlan.INSTANCE);
    }	
    
	@Override
    public <X> X unwrap(BigDecimal value, Class<X> type, WrapperOptions options) 
    {
        if (value == null)
            return null;

        if (String.class.isAssignableFrom(type))
            return (X)value.toPlainString();

        throw unknownUnwrap(type);
    }
    
    @Override
    public <X> BigDecimal wrap(X value, WrapperOptions options) 
    {
        if (value == null)
            return null;

        if(String.class.isInstance(value))
            return new BigDecimal((String)value);

        throw unknownWrap(value.getClass());
    }

	@Override
	public BigDecimal fromString(String string) 
	{
		return new BigDecimal(string);
	}
}