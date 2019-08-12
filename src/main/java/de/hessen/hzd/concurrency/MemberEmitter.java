//=====================================================
// Projekt: readhead-support-reproducer
// (c) HZD
//=====================================================
// Dateiname: MemberEmitter.java                            $
// $Revision:: 1                                     $
// $Modtime:: 12.08.2019                                $
//=====================================================

package de.hessen.hzd.concurrency;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import de.hessen.hzd.data.MemberRepository;
import de.hessen.hzd.model.Member;

/**
 * MemberEmitter
 */
@ApplicationScoped
public class MemberEmitter {

	@Inject
	private MemberRepository memberRepository;

	public List<Member> getAllMembers() {
		return memberRepository.loadMembers();
	}

}
