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
package de.hessen.hzd.data;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import de.hessen.hzd.model.Member;

@ApplicationScoped
public class MemberRepository {

	@Inject
	private Logger log;

	@Inject
	private EntityManager em;

	public Member findById(final Long id) {
		return em.find(Member.class, id);
	}

	public Member findByEmail(final String email) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Member> criteria = cb.createQuery(Member.class);
		Root<Member> member = criteria.from(Member.class);
		// Swap criteria statements if you would like to try out type-safe criteria queries, a new
		// feature in JPA 2.0
		// criteria.select(member).where(cb.equal(member.get(Member_.name), email));
		criteria.select(member).where(cb.equal(member.get("email"), email));
		return em.createQuery(criteria).getSingleResult();
	}

	public List<Member> findAllOrderedByName() {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Member> criteria = cb.createQuery(Member.class);
		Root<Member> member = criteria.from(Member.class);
		// Swap criteria statements if you would like to try out type-safe criteria queries, a new
		// feature in JPA 2.0
		// criteria.select(member).orderBy(cb.asc(member.get(Member_.name)));
		criteria.select(member).orderBy(cb.asc(member.get("name")));
		return em.createQuery(criteria).getResultList();
	}


	public List<Member> loadMembers() {

		String stmt = "select m from Member m";
		try {

			List<Member> resultList  = null;

			TypedQuery<Member> query = em.createQuery(stmt, Member.class);
			resultList = query.getResultList();

			// in order to avoid the exception do comment the previous 2 lines and uncomment the following lines:
//			@SuppressWarnings("unused")
//			Query nonTypedQuery = em.createNamedQuery(stmt);
//			resultList = query.getResultList();

			return resultList;
		} catch (Exception e) {
			log.log(Level.SEVERE, "exception when loading the members: " + e.getMessage(), e);
			return new ArrayList<>();
		}
	}

}
