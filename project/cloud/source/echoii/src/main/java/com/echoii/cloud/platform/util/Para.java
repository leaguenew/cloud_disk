package com.echoii.cloud.platform.util;

public enum Para {

	avatar, email, signature, new_password, 
	old_password, username, valid_code,
	user_id, password, token, method, target_username, 
	target_user_id, target_user_ids, sub_id, user_ids,
	parent_id, file_id, file_name, source_parent_id, 
	pub_id,
	name, email_list, 
	all, by_id, by_ids, group_id_list, groupmeta_id,
	role_id, role_id_list,

	begin, size, description, group_id, circle,
	followers, subscriptions, circles, combines,
	inbox, outbox,
	page, msg, folder,
	
	//auth method
	redis, localMap,
	
	//search
	search_type, search_content,
	
	//userinfo
	flag,
	
	//no previlege callback
	no_pre_callback,

}
