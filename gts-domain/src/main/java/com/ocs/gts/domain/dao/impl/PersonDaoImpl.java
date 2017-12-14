package com.ocs.gts.domain.dao.impl;

import org.springframework.stereotype.Repository;

import com.mysema.query.types.path.EntityPathBase;
import com.ocs.dynamo.dao.FetchJoinInformation;
import com.ocs.dynamo.dao.JoinType;
import com.ocs.dynamo.dao.impl.BaseDaoImpl;
import com.ocs.gts.domain.Person;
import com.ocs.gts.domain.QPerson;
import com.ocs.gts.domain.dao.PersonDao;

@Repository("personDao")
public class PersonDaoImpl extends BaseDaoImpl<Integer, Person> implements PersonDao {

    private QPerson qPerson = QPerson.person;

    @Override
    public Class<Person> getEntityClass() {
        return Person.class;
    }

    @Override
    protected EntityPathBase<Person> getDslRoot() {
        return qPerson;
    }

    @Override
    protected FetchJoinInformation[] getFetchJoins() {
        return new FetchJoinInformation[] { new FetchJoinInformation("organization", JoinType.INNER),
                new FetchJoinInformation("luckyNumbers"), new FetchJoinInformation("countryOfOrigin") };
    }

}
