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
import java.util.logging.Logger;

import javax.inject.Inject;

import de.hessen.hzd.data.MemberRepository;
import de.hessen.hzd.model.Member;

/**
 * SinglePollMembersTask
 */
public class SinglePollMembersTask implements Runnable {

    @Inject
    private Logger log;

    @Inject
    private MemberRepository memberRepository;

    @Override
    public void run() {

        log.info("===== (2.1): run() called ============");

        log.info("===== (2.2) ============");
        List<Member> allMembers = memberRepository.loadMembers();
        log.info("===== (2.3): Number of members:" + allMembers.size() + " ============");
    }
}
