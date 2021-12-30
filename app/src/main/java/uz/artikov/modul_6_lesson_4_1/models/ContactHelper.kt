package uz.artikov.modul_6_lesson_4_1.models

class ContactHelper {

    var id:Int? = null
    var name:String? = null
    var number:String? = null
    var text:String? = null



    constructor(id: Int?, name: String?, number: String?, text: String?) {
        this.id = id
        this.name = name
        this.number = number
        this.text = text
    }

    constructor(name: String?, number: String?, text: String?) {
        this.name = name
        this.number = number
        this.text = text
    }

    constructor()
}