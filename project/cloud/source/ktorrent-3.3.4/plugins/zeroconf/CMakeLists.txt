set(ktzeroconfplugin_SRC zeroconfplugin.cpp torrentservice.cpp)

# kde4_add_ui_files(ktzeroconfplugin_SRC zeroconfpref.ui searchbar.ui)
#kde4_add_kcfg_files(ktzeroconfplugin_SRC zeroconfpluginsettings.kcfgc)
kde4_add_plugin(ktzeroconfplugin ${ktzeroconfplugin_SRC})

target_link_libraries(ktzeroconfplugin btcore ktcore ${KDE4_KDNSSD_LIBS} ${KDE4_KPARTS_LIBS} ${QT_QT3SUPPORT_LIBRARY})
install(TARGETS ktzeroconfplugin  DESTINATION ${PLUGIN_INSTALL_DIR} )
install(FILES ktzeroconfplugin.desktop  DESTINATION  ${SERVICES_INSTALL_DIR} )

