package com.cathay.coin.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cathay.coin.pojo.Coindesk;

/**
 * Table Coindesk CRUD operation
 * 
 * @author Kim Liao
 */
public interface CoindeskDao extends JpaRepository<Coindesk, Long> {

	Long deleteByCode(String code);

	Optional<Coindesk> findOneByCode(String code);
}
