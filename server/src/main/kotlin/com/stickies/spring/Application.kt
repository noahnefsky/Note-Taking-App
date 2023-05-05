package com.stickies.spring

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.*

@SpringBootApplication
class Application

fun main(args: Array<String>) {
	runApplication<Application>(*args)
}

@RestController
@RequestMapping("/messages") // http://localhost:8080/messages
class MessageResource(val service: MessageService) {
	//RestController code
	data class StickyToSave(var id: Int, var name: String, var data: String,
								  var modified: String, var created: String)
	data class GroupToSave(var name: String, var modified: String, var created: String, var stickies: MutableList<StickyToSave>)
	@GetMapping
	fun index(): List<Message> = service.findMessages()
	@PostMapping
	fun post(@RequestBody message: Message) {
		service.post(message)
	}
	@DeleteMapping
	fun delete() = service.delete()

}

//Message Class setup
data class Message(val notes: MutableList<MessageResource.StickyToSave>,
				   val savedGroups: MutableList<MessageResource.GroupToSave>, val positionX: Double,
				   val positionY: Double, val width: Double, val height: Double)
@Service
class MessageService {
	//MessageService class setup
	var messages: MutableList<Message> = mutableListOf()
	fun findMessages() = messages
	fun post(message: Message) {
		messages.add(message)
	}
	fun delete() = messages.clear()
}