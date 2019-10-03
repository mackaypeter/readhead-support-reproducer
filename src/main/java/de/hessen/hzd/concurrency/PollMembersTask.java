//=====================================================
// Projekt: readhead-support-reproducer
// (c) HZD
//=====================================================
// Dateiname: PollMembersTask.java                            $
// $Revision:: 1                                     $
// $Modtime:: 12.08.2019                                $
//=====================================================

package de.hessen.hzd.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

/**
 * PollMembersTask
 */
public class PollMembersTask implements Callable<Integer> {

	@Inject
	private Logger log;

	@Inject
	private EntityManager em;

	@Override
	public Integer call() throws Exception {
		log.info("===== (2.2) ============");
		List<Member> allMembers = null;
		TypedQuery<Member> query = null;
		//try {
			query= em.createQuery("select m from Member m", Member.class);
			allMembers = query.getResultList();
		//} catch (Exception e) {
			//log.log(Level.SEVERE, "exception when loading the members: " + e.getMessage(), e);
			//allMembers = new ArrayList<>();
		//}
		log.info("===== (2.3): Number of members:" + allMembers.size() + " ============");
		TimeUnit.MILLISECONDS.sleep(2000);
		log.info("===== (2.4): loop stopped ============");
		return allMembers.size();
	}


}
