public class RegionData
{
	private String name;
	private String desc;
	private String billingOrginKey;
	private String billingKey;
	@SuppressWarnings("rawtypes")
	private Class clazz;

	@SuppressWarnings("rawtypes")
	public RegionData(String name, String desc, Class clazz)
	{
		this.name = name;
		this.desc = desc;
		this.clazz = clazz;
	}

	@SuppressWarnings("rawtypes")
	public Class getClazz()
	{
		return clazz;
	}

	public String getName()
	{
		return name;
	}

	public String getDesc()
	{
		return desc;
	}

	public String getBillingOrginKey()
	{
		return billingOrginKey;
	}

	public void setBillingOrginKey(String billingOrginKey)
	{
		this.billingOrginKey = billingOrginKey;
	}

	public String getBillingKey()
	{
		return billingKey;
	}

	public void setBillingKey(String billingKey)
	{
		this.billingKey = billingKey;
	}

	@Override
	public String toString()
	{
		StringBuilder sBuilder = new StringBuilder();
		sBuilder.append(this.desc);
		sBuilder.append(" | ");
		sBuilder.append(this.billingOrginKey);
		sBuilder.append(" | ");
		sBuilder.append(this.billingKey);
		return sBuilder.toString();
	}
}
