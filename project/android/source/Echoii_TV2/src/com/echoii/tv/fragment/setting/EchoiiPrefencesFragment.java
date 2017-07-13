package com.echoii.tv.fragment.setting;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.widget.Toast;

import com.echoii.tv.EchoiiTVMainActivity;
import com.echoii.tv.R;
import com.echoii.tv.constants.Constants;
import com.echoii.tv.constants.HttpConstants;
import com.echoii.tv.service.DatacenterService;
import com.echoii.tv.util.CommonUtil;
/**
 * 
 * @author Jony Zhang
 * 
 * <p>所有设置界面</p>
 *
 */
public class EchoiiPrefencesFragment extends PreferenceFragment implements 
		OnPreferenceChangeListener,
		OnSharedPreferenceChangeListener{
	
	public static final String TAG = "EchoiiPrefencesFragment";
	
	public static final String KEY_DEFAULT_SYNC_PATH = "default_sync_path";
	
	private boolean isIpWrong;
	private String defaultIp = "172.21.7.199:81";
	
	private SharedPreferences defaultPrefence;
	
	private SwitchPreference sambaSwitch;
	private SwitchPreference syncFileSwitch;
	private EditTextPreference serverIpEdtxt;
	
	private Preference syncPathPref;
	private DatacenterService service;
	
	public static EchoiiPrefencesFragment getInstance() {
		return new EchoiiPrefencesFragment();
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        defaultPrefence = PreferenceManager.getDefaultSharedPreferences(getActivity());
        defaultPrefence.registerOnSharedPreferenceChangeListener(this);
        
        service = ((EchoiiTVMainActivity)getActivity()).getService();
        
        initPreference();
    }

	private void initPreference() {
		sambaSwitch = (SwitchPreference) this.findPreference("samba_share");
		syncFileSwitch = (SwitchPreference) this.findPreference("sync_swich");
		serverIpEdtxt = (EditTextPreference) this.findPreference("server_ip");
		syncPathPref = this.findPreference("set_sync_path");
		
		syncPathPref.setSummary(defaultPrefence.getString(KEY_DEFAULT_SYNC_PATH, null));
		
		sambaSwitch.setOnPreferenceChangeListener(this);
		syncFileSwitch.setOnPreferenceChangeListener(this);
		serverIpEdtxt.setOnPreferenceChangeListener(this);
		
		serverIpEdtxt.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				if (isIpWrong) {
					serverIpEdtxt.getEditText().setText(defaultIp);
				}
				return false;
			}
		});
		
	}

	@Override
	public boolean onPreferenceChange(Preference preference, Object newValue) {
		String key = preference.getKey();
		if (key.equals("samba_share")) {
			changeSambaPreference(newValue);
		} else if (key.equals("sync_swich")) {
			Object value = newValue;
			changeSyncFiles(value);
		} else if (key.equals("server_ip")) {
			changeServerIp(newValue);
		}
		return true;
	}

	private void changeSyncFiles(Object newValue) {
		boolean flag = (Boolean) newValue;
		if (flag) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					service.startSync(Constants.SYNC_ALL_FILES);
				}
			}).start();
		} else {
			service.stopSync();
		}
	}

	private void changeServerIp(Object newValue) {
		String ip = (String) newValue;
		String regular = "\\d{1,3}+\\.\\d{1,3}+\\.\\d{1,3}+\\.\\d{1,3}\\:\\d{2,4}+";
		if (ip.matches(regular)) {
			HttpConstants.SERVER_IP = ip;
			serverIpEdtxt.setDefaultValue(ip);
			isIpWrong = false;
		} else {
			isIpWrong = true;
			HttpConstants.SERVER_IP = defaultIp;
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.sync_ip_setting_error), Toast.LENGTH_SHORT).show();
		}
	}

	private void changeSambaPreference(Object newValue) {
		boolean flag = (Boolean) newValue;
		if (flag) {
			if (!service.getJlanServerImpl().isRunning()) {
				service.startJLANServer();
			}
		} else {
			if (service.getJlanServerImpl().isRunning()) {
				service.stopJLANServer();
			}
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key == KEY_DEFAULT_SYNC_PATH) {
			String defaultPath = CommonUtil.setSyncPath(getActivity(), Constants.SYNC_SDCARD_BASE_PATH) ;
			syncPathPref.setSummary(sharedPreferences.getString(key, defaultPath));
		}
	}

}
