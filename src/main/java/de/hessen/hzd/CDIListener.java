//=====================================================
// Projekt: readhead-support-reproducer
// (c) HZD
//=====================================================
// Dateiname: CDIListener.java                            $
// $Revision:: 1                                     $
// $Modtime:: 12.08.2019                                $
//=====================================================

package de.hessen.hzd;

import java.util.concurrent.Future;
import java.util.logging.Logger;

import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.concurrent.ManagedExecutorService;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;

import de.hessen.hzd.concurrency.PollMembersTask;

/**
 * CDIListener
 */
@ApplicationScoped
public class CDIListener {

	@Inject
	private Logger log;

	@Resource(lookup = "java:jboss/ee/concurrency/executor/batchMainThreadExecutorService")
	private ManagedExecutorService executorService;

    @Inject
	private PollMembersTask task;

	private Future<?> future;

	// @formatter:off
	public void onContextInitialized(@Observes @Initialized(ApplicationScoped.class) final Object init) {
	// @formatter:on

		this.future = executorService.submit(task);

		log.info(" ===== (1): PollMembersTask started ============");
	}

	@PreDestroy
	public void preDestroy() {

		if (task != null) {
			task.stop();
		}

		if (this.future != null) {
			// https://issues.jboss.org/browse/JBEAP-6956 sonst entstehen zombies
			boolean cancelled = this.future.cancel(true);
			if (cancelled) {
				log.info("PollMembersTask stopped: true");
			} else {
				log.info("PollMembersTask stopped: false");
			}
		} else {
			log.info("PollMembersTask was not started");
		}
	}

}
