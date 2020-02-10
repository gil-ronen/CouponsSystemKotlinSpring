package com.example.couponProject.repo

import com.example.couponProject.beans.Company
import org.springframework.data.jpa.repository.JpaRepository


interface CompanyRepository : JpaRepository<Company, Int> {
    /**
     * Checks whether a company exists in the repository by name
     * @param name String
     * @return Boolean
     */
    fun existsByName(name: String): Boolean

    /**
     * Checks whether a company exists in the repository by email
     * @param email String
     * @return Boolean
     */
    fun existsByEmail(email: String): Boolean

    /**
     * Looking for a company in the repository by email
     * @param email String
     * @return Company
     */
    fun findByEmail(email: String): Company
}
