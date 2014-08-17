package com.tigamiTech.mailManagmentSystem.services;

import com.tigamiTech.mailManagmentSystem.UI.initialDataLoader;

import javax.persistence.EntityManager;

public class AuthenticationService {
    private static boolean isFirstTime= true;
	public static boolean Authenticate(String username, String password){
		EntityManager entityManager = initialDataLoader.em;

        if(isFirstTime) initialDataLoader.loadTestData();
        isFirstTime = false;
		String query = "select u from Person u where u.username='"+username+"' and u.password='"+password+"'";
		int i =0;
		try {
			 i = entityManager.createQuery(query).getResultList().size();	
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
		if(i==1) {
				return true;
		}else{
			 return false;
		}
	}
}