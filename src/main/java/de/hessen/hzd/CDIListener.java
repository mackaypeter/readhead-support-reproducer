//=====================================================
// Projekt: readhead-support-reproducer
// (c) HZD
//=====================================================
// Dateiname: CDIListener.java                            $
// $Revision:: 1                                     $
// $Modtime:: 12.08.2019                                $
//=====================================================

package de.hessen.hzd;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import javax.annotation.Resource;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.BeforeDestroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import de.hessen.hzd.concurrency.SinglePollMembersTask;

/**
 * CDIListener
 */
@ApplicationScoped
public class CDIListener {

    @Inject
    private Logger log;

    @Resource(lookup = "java:jboss/ee/concurrency/scheduler/default")
    private ScheduledExecutorService scheduledExecutorService;

    @Inject
    private Instance<SinglePollMembersTask> singlePollTaskInstance;

    private ScheduledFuture<?> scheduledFuture;

    // @formatter:off
    public void onContextInitialized(@Observes @Initialized(ApplicationScoped.class) final Object init) {
    // @formatter:on

        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(singlePollTaskInstance.get(), 2, 2, TimeUnit.SECONDS);

        log.info(" ===== (1): SinglePollMembersTask started ============");
    }

    public void onContextPreDestroyed(@Observes @BeforeDestroyed(ApplicationScoped.class) final Object beforeDestroyed) {
        log.info("CDI @ApplicationScoped context beforeDestroyed, going to cancel the SinglePollMembersTask");
        scheduledFuture.cancel(true);
    }
}
