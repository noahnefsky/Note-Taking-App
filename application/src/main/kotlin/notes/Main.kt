//Copyright (c) 2023. John Huang, Noah Nefsky, Jamie Levinson, Aryaman Goel

package notes

import javafx.application.Application
import javafx.event.EventHandler
import javafx.scene.Scene
import javafx.scene.layout.VBox
import javafx.stage.Stage
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import notes.model.GroupToSave
import notes.model.Model
import notes.view.BottomBarView
import notes.view.MenuBarView
import notes.view.NoteView
import notes.view.StickyListView
import java.io.*
import java.net.HttpURLConnection
import java.net.URI
import java.net.URL
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import java.time.LocalDateTime
import kotlin.system.exitProcess

class Main : Application() {
    @Serializable
    class PersistenceInfo(var notes: MutableList<StickyToSave>,
                          var savedGroups: MutableList<GroupToSave>,
                          var positionX: Double,
                          var positionY: Double,
                          var width: Double,
                          var height: Double)
    @Serializable
    class StickyToSave(var id: Int, var name: String, var data: String,
        var modified: String, var created: String)

    fun get(ip: String): HttpResponse<String>? {
        val client = HttpClient.newBuilder().build()
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://" + ip + ":8080/messages"))
            .GET()
            .build()
        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }

    fun post(message: String, ip: String): HttpResponse<String>? {
        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://" + ip + ":8080/messages"))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(message))
            .build()
        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }
    fun delete(ip: String): HttpResponse<String>? {
        val client = HttpClient.newBuilder().build();
        val request = HttpRequest.newBuilder()
            .uri(URI.create("http://" + ip + ":8080/messages"))
            .header("Content-Type", "application/json")
            .DELETE()
            .build()
        return client.send(request, HttpResponse.BodyHandlers.ofString())
    }
    override fun start(stage: Stage) {
        var info: String? = null
        var statuscode = 0
        var ip = "localhost"
        var serverUp = true
        var isConfig = false
        val curdir = System.getProperty("user.dir")
        try {
            isConfig = true
            val configFile = File(curdir+"/config.txt")
            ip = configFile.readLines()[0]

            // This tests to see if the server is up
            val url = URL("http://" + ip + ":8080/messages")
            val urlc = url.openConnection() as HttpURLConnection
            urlc.setRequestProperty("Connection", "close")
            urlc.connectTimeout = 1000 * 6 // Timeout is in seconds
            urlc.connect()

            var getInfo = get(ip)
            info = getInfo?.body()
            if (getInfo != null) {
                statuscode = getInfo.statusCode()
            }
        } catch (e: Exception) {
            println(e.message)
            serverUp = false
            ip = "localhost"
        }
        stage.width = 600.0
        stage.minWidth = 275.0
        val model = Model(stage)
        var isFile = true
        var persistenceInfo = PersistenceInfo(mutableListOf<StickyToSave>(),
            mutableListOf<GroupToSave>(), 0.0, 0.0, 0.0, 0.0)
        try {
            var file = FileInputStream(curdir + "/CS346 Notes Data")
            var stream = ObjectInputStream(file)
            persistenceInfo = Json.decodeFromString<PersistenceInfo>(stream.readObject().toString())
        } catch (e: Exception) {
            isFile = false

        }
        val root = VBox()
        var gotInfo = false
        if (info != null && info != "[]" && statuscode == 200) {
            var p = Json.decodeFromString<Array<PersistenceInfo>>(info)
            if (p.isNotEmpty()) persistenceInfo = p[0]
            gotInfo = true
        }
        if (gotInfo || isFile) {
            stage.x = persistenceInfo.positionX
            stage.y = persistenceInfo.positionY
            stage.height = persistenceInfo.height
            stage.width = persistenceInfo.width
            for (note in persistenceInfo.notes) {
                val stage = Stage()
                stage.setTitle(note.name)
                stage.scene = Scene(
                    NoteView(model, model.getNumOfNotes(), note.data),
                    375.0,
                    300.0
                )
                stage.minWidth = 180.0
                stage.minHeight = 130.0
//                stage.show()
                model.addSticky(note.name, stage, note.data, LocalDateTime.parse(note.modified),
                    LocalDateTime.parse(note.created))
            }
            for (group in persistenceInfo.savedGroups) {
                var addedGroup = model.addGroup(group.name2, LocalDateTime.parse(group.modified), LocalDateTime.parse(group.createdTime))
                for (sticky in group.stickies) {
                    for (s in model.getStickies()) {
                        if (sticky.id == s.id) {
                            model.addToGroup(sticky.id, addedGroup)
                        }
                    }
                }
            }
        }



        stage!!.widthProperty().addListener { event ->
            println(stage.width)
        }


        stage.onCloseRequest = EventHandler {
            try {
                val stickies = model.getStickies()
                var notes = mutableListOf<StickyToSave>()
                for (sticky in stickies) {
                    notes.add(
                        StickyToSave(
                            sticky.id, sticky.getName(), sticky.getData(),
                            sticky.modified.toString(), sticky.createdTime.toString()
                        )
                    )
                }
                persistenceInfo = PersistenceInfo(
                    mutableListOf<StickyToSave>(),
                    mutableListOf<GroupToSave>(), 0.0, 0.0, 0.0, 0.0
                )
                persistenceInfo.height = root.height
                persistenceInfo.width = root.width
                persistenceInfo.positionX = stage.x
                persistenceInfo.positionY = stage.y

                persistenceInfo.notes = notes
                for (group in model.getGroups()) {
                    var newGroupToSave = GroupToSave(
                        group.name,
                        group.modified.toString(),
                        group.createdTime.toString(),
                        mutableListOf<StickyToSave>()
                    )
                    for (sticky in group.getStickies()) {
                        newGroupToSave.stickies.add(
                            StickyToSave(
                                sticky.id, sticky.getName(), sticky.getData(),
                                sticky.modified.toString(), sticky.createdTime.toString()
                            )
                        )
                    }
                    persistenceInfo.savedGroups.add(newGroupToSave)
                }
                val stickiesJson = Json.encodeToString(persistenceInfo)
                if (serverUp) {
                    delete(ip)
                    post(stickiesJson, ip)
                }
                var file = FileOutputStream(curdir + "/CS346 Notes Data")
                var stream = ObjectOutputStream(file)
                stream.writeObject(stickiesJson)
            } catch (e: Exception) {
                println(e.message)
            }
            exitProcess(0)
        }

        stage?.scene = Scene(root)

    // our layout is the root of the scene graph
        // views are the children of the vbox
        // register views with the model

        val menuBar = MenuBarView(model)
        val list = StickyListView(model)
        val bottomBar = BottomBarView(model)
        model.addView(menuBar)
        model.addView(list)
        model.addView(bottomBar)
        model.notifyView()

        // setup and display
        root.children.add(menuBar)
        root.children.add(list)
        root.children.add(bottomBar)

        stage?.title = "Notes"
        stage?.show()
    }
}