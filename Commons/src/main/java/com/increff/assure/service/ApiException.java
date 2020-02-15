package com.increff.assure.service;

//TODO move to model
//TODO naming convention of modules
//ToDO constrain snake-Case
//ToDO getter Setter annotation place
//TODO constraint naming
//TODO annotation knowing
//TODO select by param when name is very long
//TODO binfilter service 3 methods
//TODO use crteriaquery and make 1 constructor
//TODO naming in 2 dao in 1 service
//TODO readONLY methods don't rollBAck
//TODO @transactional in every layer
//TODO space after commms and space between operator
//TODO validate binfilter better approach
//TODO  notnull and isempty com.increff.assure.util functions
//TODO use one parameter in apiexception to distinguish between errors
//TODO normalise in 1 class have different functions
//TODO getcheck in single class
//TODO add comments
//TODO better if else  for reduce
//TODO
public class ApiException extends Exception {

	private static final long serialVersionUID = 1L;

	public ApiException(String string) {
		super(string);
	}

}
