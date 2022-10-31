
package com.sblinn.userlogin.dao;

import com.sblinn.userlogin.dto.Authority;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AuthorityRepository extends CrudRepository<Authority, String> {
    
    @Query("DELETE FROM Authority a WHERE a.authority = :authority")
    void deleteAuthority(String authority);
    
}
