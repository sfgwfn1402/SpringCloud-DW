package com.dwcloud.cglib;


/**
 * CglibProxyTest
 */
public class CglibProxyTest {

    public static void main(String[] args) {
        MyCglibProxy<ActionUserDataServiceImpl> actionUserDataServiceMyCglibProxy = new MyCglibProxy<ActionUserDataServiceImpl>();

        ActionUserDataServiceImpl actionUserDataService
                = (ActionUserDataServiceImpl) actionUserDataServiceMyCglibProxy.getProxyInstance(ActionUserDataServiceImpl.class);

        actionUserDataService.addUser();
    }
}
