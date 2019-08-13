//=====================================================
// Projekt: readhead-support-reproducer
// (c) HZD
//=====================================================
// Dateiname: PollMembersTask.java                            $
// $Revision:: 1                                     $
// $Modtime:: 12.08.2019                                $
//=====================================================

package de.hessen.hzd.concurrency;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.inject.Inject;

import de.hessen.hzd.data.MemberRepository;
import de.hessen.hzd.model.Member;

/**
 * PollMembersTask
 */
public class PollMembersTask implements Callable<Integer> {

	@Inject
	private Logger log;

	@Inject
	private MemberRepository memberRepository;

	private boolean shuttingDown = false;

	@Override
	public Integer call() throws Exception {

		try {

			log.info("===== (2.1): call() called ============");

			while (!shuttingDown && !Thread.currentThread().isInterrupted()) {

				log.info("===== (2.2) ============");
				List<Member> allMembers = memberRepository.loadMembers();
				log.info("===== (2.3): Number of members:" + allMembers.size() + " ============");
				TimeUnit.MILLISECONDS.sleep(2000);
			}

			log.info("===== (2.4): loop stopped ============");
		} catch (InterruptedException e) {
			this.stop();

		} finally {
			if (Thread.currentThread().isInterrupted()) {
				if (!this.shuttingDown) {
					this.stop();

				}
			}
		}

		return Integer.MIN_VALUE;
	}

	public void stop() {
		this.shuttingDown = true;
	}

}
