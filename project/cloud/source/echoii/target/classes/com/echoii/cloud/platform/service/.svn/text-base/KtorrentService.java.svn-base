package com.echoii.cloud.platform.service;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.XMLEvent;

import org.apache.log4j.Logger;

import com.echoii.cloud.platform.entity.OfflineFileEntity;
import com.echoii.cloud.platform.entity.OfflineSubFileEntity;
import com.echoii.cloud.platform.util.Config;

public class KtorrentService {
	private static Logger log = Logger.getLogger(KtorrentService.class);
	private static volatile KtorrentService SERVICE = null;
	private Config config = Config.getInstance();
	private String server;

	public static KtorrentService getInstance() {
		if (SERVICE == null) {
			synchronized (KtorrentService.class) {
				// when more than two threads run into the first null check same
				// time, to avoid instanced more than one time, it needs to be
				// checked again.
				if (SERVICE == null) {
					SERVICE = new KtorrentService();
				}
			}
		}
		return SERVICE;
	}

	KtorrentService() {
		// init the ktorrent server if not run
		log.debug("ktorrentService begin!");
		server = config.getStringValue("offline.download.server",
				"http://172.21.7.142:8080/");
	}
	
	public static void main(String args[]){
		KtorrentService service = KtorrentService.getInstance();
		List<OfflineSubFileEntity> files = service.listSubTask("4c33f8ec9e32750964bdaea7228c4900730a6ce5");
		System.out.println(files.get(2).getName());
	}

	// return the info_hash
	public void addTask(String infoHash) {

		String request = server + "action?load_torrent=" + infoHash;
		log.debug(request);
		try {
			URL requestUrl = new URL(request);
			HttpURLConnection http = (HttpURLConnection) requestUrl.openConnection();
			http.connect();
			http.getInputStream();
			http.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public OfflineFileEntity getTask(String infoHash) {
		String api_info = server + "data/torrent/files.xml?torrent=" + infoHash;
		URL getUrl;
		try {
			getUrl = new URL(api_info);
			HttpURLConnection http = (HttpURLConnection) getUrl.openConnection();
			InputStream input = http.getInputStream();
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLEventReader reader = factory.createXMLEventReader(input);

			while (reader.hasNext()) {
				XMLEvent event = reader.nextEvent();
				if (event.isStartElement()&& event.asStartElement().getName().toString().equals("torrentinfo")) {
					
					OfflineFileEntity offline = new OfflineFileEntity();
					
					while (reader.hasNext()) {
						event = reader.nextEvent();
						
						if (event.isEndElement()&& event.asEndElement().getName().toString().equals("torrentinfo")) {
							break;
						}
						
						if (event.isStartElement()) {
							String name = event.asStartElement().getName().toString();
							String value = reader.getElementText();
							if (name.equals("name")) {
								offline.setName(value);
							} else if (name.equals("info_hash")) {
								offline.setInfoHash(value);
							} else if (name.equals("status")) {
								offline.setStatus(value);
							} else if (name.equals("total_bytes")) {
								offline.setSize(value);
							} else if (name.equals("percentage")) {
								offline.setPercents(value);
							} else if (name.equals("num_files")) {
								offline.setNumberFiles(value);
							}
						}

					}
					input.close();
					return offline;
				}
			}
			input.close();
			return null;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}

		return null;
	}

	public List<OfflineSubFileEntity> listSubTask(String infoHash) {
		String api_info = server + "data/torrent/files.xml?torrent=" + infoHash;
		URL getUrl;
		try {
			getUrl = new URL(api_info);
			HttpURLConnection http = (HttpURLConnection) getUrl.openConnection();
			InputStream input = http.getInputStream();
			XMLInputFactory factory = XMLInputFactory.newInstance();
			XMLEventReader reader = factory.createXMLEventReader(input);

			List<OfflineSubFileEntity> offSubFiles = new ArrayList<OfflineSubFileEntity>();

			while (reader.hasNext()) {
				XMLEvent event = reader.nextEvent();
				if (event.isStartElement()&& event.asStartElement().getName().toString().equals("torrentdetail")) {
					while (reader.hasNext()) {
						event = reader.nextEvent();
						
						if (event.isEndElement()&& event.asEndElement().getName().toString().equals("torrentdetail")) {
							break;
						}

						if ((event.isStartElement() && event.asStartElement().getName().toString().equals("file"))) {
							
							OfflineSubFileEntity offSubFine = new OfflineSubFileEntity();
							offSubFine.setParentInfoId(infoHash);
							while (reader.hasNext()) {
								event = reader.nextEvent();
								if (event.isEndElement()&& event.asEndElement().getName().toString().equals("file")) {
									break;
								}

								if (event.isStartElement()) {
									String name = event.asStartElement().getName().toString();
									String value = reader.getElementText();
									
									if (name.equals("path")) {
										offSubFine.setName(value);
									} else if (name.equals("percentage")) {
										offSubFine.setPercents(value);
									} else if (name.equals("size")) {
										offSubFine.setSize(value);
									}
									
								}
							}
							offSubFiles.add(offSubFine);

						}

					}
					input.close();
					return offSubFiles;
				}
			}
			input.close();

			return null;

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}

		return null;
	}

	public void removeTask(String id) {

		String request = server + "action?remove=" + id;
		try {

			URL requestUrl = new URL(request);
			HttpURLConnection http = (HttpURLConnection) requestUrl
					.openConnection();
			http.disconnect();

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public List<String> listAllTask() throws IOException, XMLStreamException {

		String api_torrents = server + "data/torrents.xml";
		URL getUrl = new URL(api_torrents);
		HttpURLConnection http = (HttpURLConnection) getUrl.openConnection();

		InputStream input = http.getInputStream();

		XMLInputFactory factory = XMLInputFactory.newInstance();
		XMLEventReader reader = factory.createXMLEventReader(input);

		List<String> list = new ArrayList<String>();

		while (reader.hasNext()) {
			XMLEvent event = reader.nextEvent();
			if (event.isStartElement()) {
				String name = event.asStartElement().getName().toString();
				if (name.equals("info_hash")) {
					String text = reader.getElementText();
					list.add(text);
				}
			}
		}

		input.close();
		return list;
	}

}
