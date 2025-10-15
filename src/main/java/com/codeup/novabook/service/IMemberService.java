package com.codeup.novabook.service;

import com.codeup.novabook.domain.Member;
import com.codeup.novabook.domain.MemberRole;
import com.codeup.novabook.exception.DatabaseException;

import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.Optional;

public interface IMemberService {
    Member create(Member member) throws DatabaseException;
    Member update(Member member) throws DatabaseException;
    boolean softDelete(Integer id) throws DatabaseException;
    boolean hardDelete(Integer id) throws DatabaseException;

    Optional<Member> findById(Integer id) throws DatabaseException;
    List<Member> findAll() throws DatabaseException;
    List<Member> findAllActive() throws DatabaseException;
    List<Member> findByName(String name) throws DatabaseException;
    List<Member> findByRole(MemberRole role) throws DatabaseException;
    List<Member> findActiveByRole(MemberRole role) throws DatabaseException;

    boolean updateActiveStatus(Integer memberId, Boolean active) throws DatabaseException;

    int importFromCsv(Reader reader) throws Exception;
    void exportToCsv(Writer writer) throws Exception;
}
