package com.kodgames.authserver.service.global;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import com.kodgames.authserver.config.AuthServerConfig;
import com.kodgames.authserver.config.AuthZdbConfig;
import com.kodgames.corgi.core.service.PublicService;

import xbean.ClientVersion;
import xbean.LibVersion;
import xbean.ProVersion;
import xbean.VersionUpdateBean;

public class GlobalService extends PublicService
{
	private static final long serialVersionUID = -6136174944753918219L;

	private static Logger logger = LoggerFactory.getLogger(GlobalService.class);

	// private static final int SUCCESS = 1;
	// private static final int FAIL = 0;

	private synchronized ClientVersion getClientVersion()
	{
		ClientVersion cv = table.Global_client_version.select(AuthServerConfig.TABLE_CLIENT_VERSION_KEY);
		if (null == cv)
		{
			cv = table.Global_client_version.insert(AuthServerConfig.TABLE_CLIENT_VERSION_KEY);
		}

		return cv;
	}

	public ProVersion getProductVersion()
	{
		return getClientVersion().getProVersion();
	}

	public LibVersion getLibraryVersion(final String platform)
	{
		Map<String, LibVersion> lvs = getClientVersion().getLibVersions();
		LibVersion lv = lvs.get(platform);
		if (null == lv)
		{
			lv = new LibVersion();
			lv.setVersion("");
			lv.setDescription("");
			lv.setUrl("");
			lv.setForceUpdate(false);
			lvs.put(platform, lv);
		}
		return lv;
	}

	public int updateProductVersion(final String version, final String description)
	{
		ProVersion pv = getProductVersion();
		pv.setVersion(version);
		pv.setDescription(description);

		return 1;
	}

	public ProVersion queryProductVersion()
	{
		return getClientVersion().getProVersion();
	}

	public int insertLibraryVersion(final String platform, final String version, final String description,
		final String url, final boolean forceUpdate)
	{
		Map<String, LibVersion> lvs = getClientVersion().getLibVersions();
		if (lvs.containsKey(platform))
		{
			return 0;
		}

		LibVersion lv = new LibVersion();
		lv.setVersion(version);
		lv.setDescription(description);
		lv.setUrl(url);
		lv.setForceUpdate(forceUpdate);
		lvs.put(platform, lv);

		return 1;
	}

	public void setUpdateVersion(final String id, final String channel, final String subchannel,
		final String libVersion, final String lastLibVersion, final String libUrl, final String proVersion,
		final String proUrl, final String reviewVersion, final String reviewUrl, final Boolean proForceUpdate)
	{
		VersionUpdateBean versionUpdateBean = table.Channel_version_table.select(id);
		if (versionUpdateBean == null)
		{
			versionUpdateBean = table.Channel_version_table.insert(id);
		}

		VersionUpdateBean vub = versionUpdateBean;
		vub.setChannel(channel);
		vub.setSubchannel(subchannel);
		vub.setLibVersion(libVersion);
		vub.setLastLibVersion(lastLibVersion);
		vub.setLibUrl(libUrl);
		vub.setProVersion(proVersion);
		vub.setProUrl(proUrl);
		vub.setReviewVersion(reviewVersion);
		vub.setReviewUrl(reviewUrl);
		vub.setProForceUpdate(proForceUpdate);
		Map<String, Long> versionUpdateKeys = AuthZdbConfig.getInstance().versionUpdateKeys;
		long now = System.currentTimeMillis();
		versionUpdateKeys.put(id, new Long(now));
		return;
	}

	public List<HashMap<String, Object>> getUpdateVersion()
	{
		List<HashMap<String, Object>> records = new ArrayList<HashMap<String, Object>>();

		// table.Channel_version_table.get().walk((key, value) -> {
		table.Channel_version_table.get().getCache().walk((key, value) -> {
			HashMap<String, Object> record = new HashMap<String, Object>();
			record.put("channel", value.getChannel());
			record.put("subchannel", value.getSubchannel());
			record.put("libVersion", value.getLibVersion());
			record.put("lastLibVersion", value.getLastLibVersion());
			record.put("libUrl", value.getLibUrl());
			record.put("proVersion", value.getProVersion());
			record.put("proUrl", value.getProUrl());
			record.put("reviewVersion", value.getReviewVersion());
			record.put("reviewUrl", value.getReviewUrl());
			record.put("proForceUpdate", value.getProForceUpdate());
			records.add(record);
		});

		if (!records.isEmpty())
		{
			return records;
		}

		Map<String, Long> versionUpdateKeys = AuthZdbConfig.getInstance().versionUpdateKeys;
		for (String id : versionUpdateKeys.keySet())
		{
			VersionUpdateBean value = table.Channel_version_table.select(id);
			if (value == null)
			{
				continue;
			}

			HashMap<String, Object> record = new HashMap<String, Object>();
			record.put("channel", value.getChannel());
			record.put("subchannel", value.getSubchannel());
			record.put("libVersion", value.getLibVersion());
			record.put("lastLibVersion", value.getLastLibVersion());
			record.put("libUrl", value.getLibUrl());
			record.put("proVersion", value.getProVersion());
			record.put("proUrl", value.getProUrl());
			record.put("reviewVersion", value.getReviewVersion());
			record.put("reviewUrl", value.getReviewUrl());
			record.put("proForceUpdate", value.getProForceUpdate());
			records.add(record);
		}
		return records;
	}

	public void removeUpdateVersion(String id)
	{
		Map<String, Long> versionUpdateKeys = AuthZdbConfig.getInstance().versionUpdateKeys;
		versionUpdateKeys.remove(id);

		
		if (!table.Channel_version_table.delete(id))
		{
			logger.warn("removeUpdateVersion, this Version has Been Deleted Before!");
		}

		return;
	}

	public int deleteLibraryVersion(String platform)
	{
		getClientVersion().getLibVersions().remove(platform);

		return 1;
	}

	public int updateLibraryVersion(final String platform, final String version, final String description,
		final String url, final boolean forceUpdate)
	{
		LibVersion lv = getClientVersion().getLibVersions().get(platform);
		if (null == lv)
		{
			return 0;
		}

		lv.setVersion(version);
		lv.setDescription(description);
		lv.setUrl(url);
		lv.setForceUpdate(forceUpdate);

		return 1;
	}

	public Map<String, LibVersion> queryLibraryVersion()
	{
		return getClientVersion().getLibVersions();
	}

	public boolean isProductVersionNeedUpdate(String version)
	{
		ProVersion pv = getProductVersion();
		try
		{
			int serverVersion = Integer.parseInt(pv.getVersion());
			int clientVersion = Integer.parseInt(version);
			return clientVersion < serverVersion;
		}
		catch (Throwable t)
		{
			return true;
		}
	}

	public boolean compareVersion(String clientVersion, String serverVersion)
	{
		String[] versionArray1 = clientVersion.split("\\.");
		String[] versionArray2 = serverVersion.split("\\.");
		int idx = 0;
		int minLength = Math.min(versionArray1.length, versionArray2.length);
		int diff = 0;
		while (idx < minLength && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0
			&& (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0)
		{
			++idx;
		}
		diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
		return diff < 0;
	}

	public boolean isLibraryVersionNeedUpdate(String platform, String version)
	{
		LibVersion lv = getLibraryVersion(platform);
		try
		{
			if (lv.getVersion() == null || lv.getVersion().equals("") )
			{
				return false;
			}
			
			if (version == null)
			{
				return true;
			} 

			return  compareVersion( version , lv.getVersion() );
		}
		catch (Throwable t)
		{
			return true;
		}
	}

}
