package com.kodgames.authserver.service.global;

import com.kodgames.corgi.core.service.ServiceContainer;
import com.kodgames.message.proto.auth.AuthProtoBuf.LibraryVersionPROTO;
import com.kodgames.message.proto.auth.AuthProtoBuf.ProductVersionPROTO;

import xbean.LibVersion;
import xbean.ProVersion;

public class GlobalData
{
	public static ProductVersionPROTO proVersionBeanToProto(final ProVersion bean, final String version)
	{
		GlobalService service = ServiceContainer.getInstance().getPublicService(GlobalService.class);
		return ProductVersionPROTO.newBuilder()
			.setNeedUpdate(service.isProductVersionNeedUpdate(version))
			.setVersion(bean.getVersion())
			.setDescription(bean.getDescription())
			.build();
	}

	public static LibraryVersionPROTO libVersionBeanToProto(final LibVersion bean, final String platform, final String version)
	{
		GlobalService service = ServiceContainer.getInstance().getPublicService(GlobalService.class);
		boolean isNeedForUpdate = service.isLibraryVersionNeedUpdate(platform, version); 
		return LibraryVersionPROTO.newBuilder()
			.setForceUpdate(isNeedForUpdate && bean.getForceUpdate())
			.setNeedUpdate(isNeedForUpdate)
			.setVersion(bean.getVersion())
			.setDescription(bean.getDescription())
			.setUrl(bean.getUrl())
			.build();
	}
}
