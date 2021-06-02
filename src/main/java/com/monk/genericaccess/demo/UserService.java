package com.monk.genericaccess.demo;

import com.monk.genericaccess.exception.IllegalAnnotationException;
import com.monk.genericaccess.filter.FilterOption;
import com.monk.genericaccess.filter.FilterOrder;
import com.monk.genericaccess.filter.ObjectFilter;
import com.monk.genericaccess.GenericAccess;
import com.monk.genericaccess.GenericAccessFactory;

import java.util.List;

public class UserService {

    private static final GenericAccess<User> userAccess = GenericAccessFactory.getInstance(User.class);

    public User getUser(String userId){
        User user = null;
        try{
            user = userAccess.select(userId);
        }catch (Exception e) {
            e.printStackTrace();
        }

        assert user != null;
        System.out.println(user.getUserId());
        return user;
    }

    public List<User> getByPid(String pid) throws IllegalAnnotationException {
        ObjectFilter<User> filter = new ObjectFilter<>();
//        filter.add(FilterOption.and(FilterOption.eq("userPw", "testPw")));
        filter.add(FilterOption.like("userId", "te%"));

        return userAccess.selectWithFilter(filter);
    }

    public List<User> searchId(String ...keyWords) throws IllegalAnnotationException {
        ObjectFilter<User> filter = userAccess.getFilter();
        for (String s: keyWords) {
            filter.add(FilterOption.like("userId", s + "%"));
        }

        return userAccess.selectWithFilter(filter);
    }

    public List<User> searchWithPid(String ...keyWords) throws IllegalAnnotationException {
        ObjectFilter<User> filter = userAccess.getFilter();
        for (String s: keyWords) {
            filter.add(FilterOption.like("pid", "%"+s + "%"));
        }

        return userAccess.selectWithFilter(filter);
    }

    public List<User> searchPidLimit(String ...keyWords) throws IllegalAnnotationException {
        ObjectFilter<User> filter = userAccess.getFilter();
        for (String s: keyWords) {
            filter.add(FilterOption.like("pid", "%"+s + "%"));
        }

        filter.limit(2);
        return userAccess.selectWithFilter(filter);
    }

    public List<User> searchUserOrder(String key, String order) throws IllegalAnnotationException {
        ObjectFilter<User> filter = userAccess.getFilter();
        filter.add(FilterOption.like("userId", "%" + key+"%"));
        filter.add(FilterOrder.asc(order));

        return userAccess.selectWithFilter(filter);
    }


}
