# This file should contain all the record creation needed to seed the database with its default values.
# The data can then be loaded with the rake db:seed (or created alongside the db with db:setup).
#
# Examples:
#
#   cities = City.create([{ name: 'Chicago' }, { name: 'Copenhagen' }])
#   Mayor.create(name: 'Emanuel', city: cities.first)

tipotrabajo1 = Tipotrabajo.new
tipotrabajo1.id = 1
tipotrabajo1.nombre = "albanil"
tipotrabajo1.descripcion = ""
tipotrabajo1.save


tipotrabajo2 = Tipotrabajo.new
tipotrabajo2.id = 2
tipotrabajo2.nombre = "jardinero"
tipotrabajo2.descripcion = ""
tipotrabajo2.save

tipotrabajo3 = Tipotrabajo.new
tipotrabajo3.id = 3
tipotrabajo3.nombre = "plomero"
tipotrabajo3.descripcion = ""
tipotrabajo3.save

tipotrabajo4 = Tipotrabajo.new
tipotrabajo4.id = 4
tipotrabajo4.nombre = "otros"
tipotrabajo4.descripcion = ""
tipotrabajo4.save