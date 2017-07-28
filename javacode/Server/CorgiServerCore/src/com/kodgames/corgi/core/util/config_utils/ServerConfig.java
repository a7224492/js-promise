package com.kodgames.corgi.core.util.config_utils;

import com.kodgames.message.proto.server.ServerProtoBuf;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lz on 2016/8/17.
 * 通用ServerConfig
 * <p>
 * 所有listen是server用来监听本身的
 * 所有address是用于给client用于连接的
 * address如果没有缺省设置则使用同名listen
 */
public class ServerConfig
{

	private String type;

	//id起始
	private String id;

	private String listen_socket_for_server;

	private String listen_http_for_server;

	private String listen_http_for_gmt;

	private String listen_http_for_client;

	private String listen_web_socket_for_client;

	private String listen_socket_for_client;

	private String address_socket_for_client;

	private String address_http_for_client;

	private String address_web_socket_for_client;

	private String address_socket_for_server;

	private String address_http_for_server;

	private String address_http_for_gmt;

	private Map<String, DbConfig> dbs;

	private int area;

	public void addDbConfig(String key, String address, String name, String user, String pwd)
	{
		DbConfig db = new DbConfig();
		db.address = address;
		db.name = name;
		db.user = user;
		db.password = pwd;
		addDbConfig(key, db);
	}

	public ServerProtoBuf.ServerConfigPROTO toProto()
	{
		ServerProtoBuf.ServerConfigPROTO.Builder p = ServerProtoBuf.ServerConfigPROTO.newBuilder();
		p.setId(this.getId());
		p.setType(this.getType());

		if (getListen_http_for_client() != null)
			p.setListenHttpForClient(this.getListen_http_for_client().getAddressString());

		if (getListen_http_for_server() != null)
			p.setListenHttpForServer(this.getListen_http_for_server().getAddressString());

		if (getListen_http_for_gmt() != null)
			p.setListenHttpForGmt(this.getListen_http_for_gmt().getAddressString());

		if (getListen_http_for_client() != null)
			p.setListenHttpForClient(this.getListen_http_for_client().getAddressString());

		if (getListen_socket_for_server() != null)
			p.setListenSocketForServer(this.getListen_socket_for_server().getAddressString());

		if (getListen_web_socket_for_client() != null)
			p.setListenWebSocketForClient(this.getListen_web_socket_for_client().getAddressString());

		if (getListen_socket_for_client() != null)
			p.setListenSocketForClient(this.getListen_socket_for_client().getAddressString());

		if (getAddress_http_for_client() != null)
			p.setAddressHttpForClient(this.getAddress_http_for_client().getAddressString());

		if (getAddress_http_for_gmt() != null)
			p.setAddressHttpForGmt(this.getAddress_http_for_gmt().getAddressString());

		if (getAddress_http_for_server() != null)
			p.setAddressHttpForServer(this.getAddress_http_for_server().getAddressString());

		if (getAddress_socket_for_client() != null)
			p.setAddressSocketForClient(this.getAddress_socket_for_client().getAddressString());

		if (getAddress_web_socket_for_client() != null)
			p.setAddressWebSocketForClient(this.getAddress_web_socket_for_client().getAddressString());

		if (getAddress_socket_for_server() != null)
			p.setAddressSocketForServer(this.getAddress_socket_for_server().getAddressString());


		p.setArea(this.getArea());

		Map<String, ServerConfig.DbConfig> dbs = this.getDbConfigs();
		dbs.entrySet().stream().forEach(entry -> p.addDbs(ServerProtoBuf.DbPROTO.newBuilder().setType(entry.getKey()).setName(entry.getValue().name).setAddress(entry.getValue().address).setUser(entry.getValue().user).setPassword(entry.getValue().password)));
		return p.build();
	}

	public static ServerConfig fromProto(ServerProtoBuf.ServerConfigPROTO p)
	{

		ServerConfig r = new ServerConfig();
		r.setId(String.valueOf(p.getId()));
		r.setType(String.valueOf(p.getType()));
		r.setListen_http_for_server(p.getListenHttpForServer());
		r.setListen_http_for_gmt(p.getListenHttpForGmt());
		r.setListen_http_for_client(p.getListenHttpForClient());
		r.setListen_socket_for_server(p.getListenSocketForServer());
		r.setListen_web_socket_for_client(p.getListenWebSocketForClient());
		r.setListen_socket_for_client(p.getListenSocketForClient());

		r.setAddress_http_for_client(p.getAddressHttpForClient());
		r.setAddress_http_for_gmt(p.getAddressHttpForGmt());
		r.setAddress_http_for_server(p.getAddressHttpForServer());
		r.setAddress_socket_for_client(p.getAddressSocketForClient());
		r.setAddress_web_socket_for_client(p.getAddressWebSocketForClient());
		r.setAddress_socket_for_server(p.getAddressSocketForServer());
		r.setArea(p.getArea());

		List<ServerProtoBuf.DbPROTO> dbs = p.getDbsList();
		dbs.stream().forEach(db -> r.addDbConfig(db.getType(), db.getAddress(), db.getName(), db.getUser(), db.getPassword()));
		return r;
	}

	public void addDbConfig(String key, DbConfig db)
	{
		dbs.put(key, db);
	}

	public Map<String, DbConfig> getDbConfigs()
	{
		return dbs;
	}

	public ServerConfig()
	{
		dbs = new HashMap<>();
	}

	public void setType(String type)
	{
		this.type = type;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public void setListen_socket_for_server(String listen_socket_for_server)
	{
		this.listen_socket_for_server = listen_socket_for_server;
	}

	public void setListen_http_for_server(String listen_http_for_server)
	{
		this.listen_http_for_server = listen_http_for_server;
	}

	public void setListen_http_for_gmt(String listen_http_for_gmt)
	{
		this.listen_http_for_gmt = listen_http_for_gmt;
	}

	public void setListen_http_for_client(String listen_http_for_client)
	{
		this.listen_http_for_client = listen_http_for_client;
	}

	public void setListen_web_socket_for_client(String listen_web_socket_for_client)
	{
		this.listen_web_socket_for_client = listen_web_socket_for_client;
	}

	public void setListen_socket_for_client(String listen_socket_for_client)
	{
		this.listen_socket_for_client = listen_socket_for_client;
	}

	public void setAddress_socket_for_client(String address_socket_for_client)
	{
		this.address_socket_for_client = address_socket_for_client;
	}

	public void setAddress_http_for_client(String address_http_for_client)
	{
		this.address_http_for_client = address_http_for_client;
	}

	public void setAddress_web_socket_for_client(String address_web_socket_for_client)
	{
		this.address_web_socket_for_client = address_web_socket_for_client;
	}

	public void setAddress_socket_for_server(String address_socket_for_server)
	{
		this.address_socket_for_server = address_socket_for_server;
	}

	public void setAddress_http_for_server(String address_http_for_server)
	{
		this.address_http_for_server = address_http_for_server;
	}

	public void setAddress_http_for_gmt(String address_http_for_gmt)
	{
		this.address_http_for_gmt = address_http_for_gmt;
	}

	public int getType()
	{
		return Integer.valueOf(type);
	}

	public int getId()
	{
		return id == null ? 0 : Integer.valueOf(id);
	}

	public AddressConfig getListen_socket_for_server()
	{
		return isNull(listen_socket_for_server) ? null : new AddressConfig(listen_socket_for_server);
	}

	public AddressConfig getListen_http_for_server()
	{
		return isNull(listen_http_for_server) ? null : new AddressConfig(listen_http_for_server);
	}

	public AddressConfig getListen_http_for_gmt()
	{
		return isNull(listen_http_for_gmt) ? null : new AddressConfig(listen_http_for_gmt);
	}

	public AddressConfig getListen_http_for_client()
	{
		return isNull(listen_http_for_client) ? null : new AddressConfig(listen_http_for_client);
	}

	public AddressConfig getListen_web_socket_for_client()
	{
		return isNull(listen_web_socket_for_client) ? null : new AddressConfig(listen_web_socket_for_client);
	}

	public AddressConfig getListen_socket_for_client()
	{
		return isNull(listen_socket_for_client) ? null : new AddressConfig(listen_socket_for_client);
	}

	public AddressConfig getAddress_socket_for_client()
	{
		return isNull(address_socket_for_client) ? getListen_socket_for_client() : new AddressConfig(address_socket_for_client);
	}

	public AddressConfig getAddress_http_for_client()
	{
		return isNull(address_http_for_client) ? getListen_http_for_client() : new AddressConfig(address_http_for_client);
	}

	public AddressConfig getAddress_web_socket_for_client()
	{
		return isNull(address_web_socket_for_client) ? getListen_web_socket_for_client() : new AddressConfig(address_web_socket_for_client);
	}

	public AddressConfig getAddress_socket_for_server()
	{
		return isNull(address_socket_for_server) ? getListen_socket_for_server() : new AddressConfig(address_socket_for_server);
	}

	public AddressConfig getAddress_http_for_server()
	{
		return isNull(address_http_for_server) ? getListen_http_for_server() : new AddressConfig(address_http_for_server);
	}

	public AddressConfig getAddress_http_for_gmt()
	{
		return isNull(address_http_for_gmt) ? getListen_http_for_gmt() : new AddressConfig(address_http_for_gmt);
	}

	public int getArea()
	{
		return area;
	}

	public void setArea(int area)
	{
		this.area = area;
	}

	private boolean isNull(String str)
	{
		return str == null || str.isEmpty() || ((new AddressConfig(str).getHost() == null) && (new AddressConfig(str).getPort() == 0));
	}

	@Override public String toString()
	{
		return "ServerConfig{" + "type=" + type +
			", id=" + id +
			", listen_socket_for_server='" + listen_socket_for_server + '\'' +
			", listen_http_for_server='" + listen_http_for_server + '\'' +
			", listen_http_for_gmt='" + listen_http_for_gmt + '\'' +
			", listen_http_for_client='" + listen_http_for_client + '\'' +
			", listen_web_socket_for_client='" + listen_web_socket_for_client + '\'' +
			", listen_socket_for_client='" + listen_socket_for_client + '\'' +
			", address_socket_for_client='" + address_socket_for_client + '\'' +
			", address_http_for_client='" + address_http_for_client + '\'' +
			", address_web_socket_for_client='" + address_web_socket_for_client + '\'' +
			", address_socket_for_server='" + address_socket_for_server + '\'' +
			", address_http_for_server='" + address_http_for_server + '\'' +
			", address_http_for_gmt='" + address_http_for_gmt + '\'' +
			'}';
	}

	public static class DbConfig
	{
		public String name;
		public String address;
		public String user;
		public String password;
	}
}
