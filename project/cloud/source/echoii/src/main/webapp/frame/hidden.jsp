<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<form id="share-download-form" method="get" target="dl-frame" action="" style="display:none;">
      <input id="sdf-file-id" type="hidden" name="file_id"  />
      <input id="sdf-idcode" type="hidden" name="idcode"  />
</form>

<form id="share-download-form-nocode" method="get" target="dl-frame" action="" style="display:none;">
      <input id="sdf-file-id-nocode" type="hidden" name="file_id"  />
</form>


<form id="download-form" method="get" target="dl-frame" action="" style="display:none;">
      <input id="df-file-id" type="hidden" name="file_id"  />
      <input id="df-user-id" type="hidden" name="user_id"  />
      <input id="df-token" type="hidden" name="token" />
      <input id="df-err-cb" type="hidden" name="callback"  />
      <input id="df-file-name" type="hidden" name="file_name" />
</form>

<form id="packdownload-form" method="get" target="dl-frame" action="" style="display:none;">
      <input id="pf-file-id-list" type="hidden" name="file_id_list"  />
      <input id="pf-user-id" type="hidden" name="user_id"  />
      <input id="pf-token" type="hidden" name="token" />
      <input id="pf-err-cb" type="hidden" name="err_callback"  />
      <input id="pf-file-name" type="hidden" name="file_name" />
</form>

<iframe id='dl-frame' name='dl-frame' src='' style='display: none'></iframe>

<input id="upload-file-input" type="file" style='display: none' ></input>