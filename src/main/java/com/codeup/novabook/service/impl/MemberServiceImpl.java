package com.codeup.novabook.service.impl;

import com.codeup.novabook.domain.Member;
import com.codeup.novabook.domain.MemberRole;
import com.codeup.novabook.exception.DatabaseException;
import com.codeup.novabook.repository.IMemberRepository;
import com.codeup.novabook.service.IMemberService;
import com.codeup.novabook.util.csv.MemberCsv;

import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Optional;

public class MemberServiceImpl implements IMemberService {

    private final IMemberRepository repo;

    public MemberServiceImpl(IMemberRepository repo) {
        this.repo = repo;
    }

    @Override
    public Member create(Member member) throws DatabaseException { return repo.save(member); }

    @Override
    public Member update(Member member) throws DatabaseException { return repo.update(member); }

    @Override
    public boolean softDelete(Integer id) throws DatabaseException { return repo.softDelete(id); }

    @Override
    public boolean hardDelete(Integer id) throws DatabaseException { return repo.hardDelete(id); }

    @Override
    public Optional<Member> findById(Integer id) throws DatabaseException { return repo.findById(id); }

    @Override
    public List<Member> findAll() throws DatabaseException { return repo.findAll(); }

    @Override
    public List<Member> findAllActive() throws DatabaseException { return repo.findAllActive(); }

    @Override
    public List<Member> findByName(String name) throws DatabaseException { return repo.findByName(name); }

    @Override
    public List<Member> findByRole(MemberRole role) throws DatabaseException { return repo.findByRole(role); }

    @Override
    public List<Member> findActiveByRole(MemberRole role) throws DatabaseException { return repo.findActiveByRole(role); }

    @Override
    public boolean updateActiveStatus(Integer memberId, Boolean active) throws DatabaseException { return repo.updateActiveStatus(memberId, active); }

    @Override
    public int importFromCsv(Reader reader) throws Exception {
        List<Member> members = MemberCsv.read(reader);
        int count = 0;
        for (Member m : members) {
            // Without a unique field in Member, just save them
            repo.save(m);
            count++;
        }
        return count;
    }

    @Override
    public void exportToCsv(Writer writer) throws Exception { MemberCsv.write(repo.findAll(), writer); }
}
