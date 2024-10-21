package com.eureka.mindbloom.category.repository;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.eureka.mindbloom.Fixtures.ChildFixture;
import com.eureka.mindbloom.Fixtures.ChildPreferredFixture;
import com.eureka.mindbloom.Fixtures.ParentFixture;
import com.eureka.mindbloom.category.domain.ChildPreferred;
import com.eureka.mindbloom.member.domain.Child;
import com.eureka.mindbloom.member.domain.Member;
import com.eureka.mindbloom.member.repository.ChildRepository;
import com.eureka.mindbloom.member.repository.MemberRepository;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class ChildPreferredRepositoryTest {
	@Autowired
	private ChildPreferredRepository childPreferredRepository;

	@Autowired
	private ChildRepository childRepository;

	@Autowired
	private MemberRepository memberRepository;

	private Child savedChild;

	@BeforeAll
	public void setUp() {
		childPreferredRepository.deleteAll();
		childRepository.deleteAll();
		memberRepository.deleteAll();
		Member member = ParentFixture.getParent();
		Member savedMember = memberRepository.save(member);

		Child child = ChildFixture.getChild(savedMember);
		savedChild = childRepository.save(child);
	}

	@Test
	public void 아이가_선호하는_콘텐츠가_저장된다() {
		// Given
		List<ChildPreferred> childPreferredList = ChildPreferredFixture.getChildPreferredList(savedChild);
		childPreferredRepository.saveAll(childPreferredList);

		// When
		List<ChildPreferred> result = childPreferredRepository.findAll();

		// Then
		assertThat(result.size()).isEqualTo(childPreferredList.size());
	}
}
