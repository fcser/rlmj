package cn.java.rlmj.service.impl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.java.rlmj.dao.PeopleMapper;
import cn.java.rlmj.pojo.People;
import cn.java.rlmj.sdkdo.Face;
import cn.java.rlmj.service.PeopleService;

@Service("peopleService")
@Transactional
public class PeopleServiceImpl implements PeopleService {
	@Autowired
	private PeopleMapper peopleMapper;

	@Override
	public ArrayList<Face> getModels() {
		return peopleMapper.getModels();
	}

	@Override
	public int deletePeople(int personid) {
		return peopleMapper.deletePeople(personid);
	}

	@Override
	public People getPeopleById(int id) {
		return peopleMapper.getPeopleById(id);
	}

	@Override
	public int insertPeople(People people) {
		return peopleMapper.insertPeople(people);
	}

	@Override
	public int updatePeople(People people) {
		// TODO Auto-generated method stub
		return peopleMapper.updatePeople(people);
	}

	@Override
	public String getPicUrl(int people_id) {
		// TODO Auto-generated method stub
		return peopleMapper.getPicUrl(people_id);
	}

	@Override
	public ArrayList<People> getPeoples(People people) {
		// TODO Auto-generated method stub
		return peopleMapper.getPeople(people);
	}

}
