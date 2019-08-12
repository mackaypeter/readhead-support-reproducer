/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.hessen.hzd.util;

import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Default;
import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.faces.context.FacesContext;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.persistence.EntityManager;

/**
 * This class uses CDI to alias Java EE resources, such as the persistence context, to CDI beans
 *
 * <p>
 * Example injection on a managed bean field:
 * </p>
 *
 * <pre>
 * &#064;Inject
 * private EntityManager em;
 * </pre>
 */
public class Resources {

	// wird als property in persistence.xml gesetzt
	private static final String ENTITY_MANAGER_JNDI_NAME = "java:/EntityManager/Reproducer_Persistence";

	@Produces
	@Default
//	@RequestScoped
	public EntityManager lookupEntityManager() {

		try {
			final EntityManager em;
			final InitialContext ictx = new InitialContext();
			try {
				em = (EntityManager) ictx.lookup(ENTITY_MANAGER_JNDI_NAME);
				if (em == null) {
					throw new IllegalStateException("Could not get the EntityManager '" + ENTITY_MANAGER_JNDI_NAME + "'");
				}
				return em;
			} finally {
				ictx.close();
			}
		} catch (NamingException e) {
			throw new IllegalStateException(
				"A NamingException occured, could not get the EntityManager '" + ENTITY_MANAGER_JNDI_NAME + "'", e);
		}
	}

    @Produces
    public Logger produceLog(final InjectionPoint injectionPoint) {
        return Logger.getLogger(injectionPoint.getMember().getDeclaringClass().getName());
    }

    @Produces
    @RequestScoped
    public FacesContext produceFacesContext() {
        return FacesContext.getCurrentInstance();
    }

}
