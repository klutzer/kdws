package br.net.twome.kd.dao;

import org.mentabean.BeanSession;

public abstract class AbstractDAO {

	protected BeanSession session;

	public void setBeanSession(BeanSession session) {
		this.session = session;
	}
	
	public BeanSession getBeanSession() {
		return session;
	}
}
