//Copyright (c) 2023. John Huang, Noah Nefsky, Jamie Levinson, Aryaman Goel

package notes.model

import kotlinx.serialization.Serializable
import notes.Main
import java.time.LocalDateTime
@Serializable
class GroupToSave(var name: String, var modified: String, var created: String,
                  var stickies: MutableList<Main.StickyToSave>) {
    var stickies2 = stickies
    var name2 = name
    var modified2 = modified
    var createdTime = created
}
class Group(nameGiven: String, modifiedLast: LocalDateTime, created: LocalDateTime) {
    private var stickies = mutableListOf<Sticky>()
    var name = nameGiven
    var modified = modifiedLast
    var createdTime = created
//    fun getName(): String {
//        return name
//    }
    fun addSticky(sticky: Sticky) {
        stickies.add(sticky)
    }
    fun getStickies(): MutableList<Sticky> {
        return stickies
    }

}