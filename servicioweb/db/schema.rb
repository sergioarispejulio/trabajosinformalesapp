# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20160331121030) do

  create_table "comentarioaclientes", force: :cascade do |t|
    t.string   "comentario"
    t.string   "nombre_usuario_que_comenta"
    t.integer  "id_trabajador_que_comenta"
    t.integer  "id_usuario_que_recibe_comentario"
    t.datetime "created_at",                       null: false
    t.datetime "updated_at",                       null: false
  end

  create_table "comentarioatrabajadors", force: :cascade do |t|
    t.string   "comentario"
    t.string   "nombre_usuario_que_comenta"
    t.integer  "id_usuario_que_comenta"
    t.integer  "id_trabajador_que_recibe_comentario"
    t.integer  "id_perfil_trabajo"
    t.datetime "created_at",                          null: false
    t.datetime "updated_at",                          null: false
  end

  create_table "conexionescelulars", force: :cascade do |t|
    t.string   "token"
    t.string   "token_mensajeria"
    t.string   "modelo_celular"
    t.string   "sistema_operativo"
    t.date     "fecha_login"
    t.date     "fecha_logout"
    t.integer  "id_usuario"
    t.datetime "created_at",        null: false
    t.datetime "updated_at",        null: false
  end

  create_table "etiqueta", force: :cascade do |t|
    t.string   "id_tipo_de_trabajo"
    t.string   "nombre"
    t.datetime "created_at",         null: false
    t.datetime "updated_at",         null: false
  end

  create_table "etiquetatrabajos", force: :cascade do |t|
    t.integer  "id_etiqueta"
    t.integer  "id_trabajo"
    t.datetime "created_at",  null: false
    t.datetime "updated_at",  null: false
  end

  create_table "perfiltrabajadors", force: :cascade do |t|
    t.integer  "puntaje_promedio"
    t.integer  "id_usuario"
    t.integer  "id_tipo_trabajo"
    t.boolean  "habilitado"
    t.datetime "created_at",       null: false
    t.datetime "updated_at",       null: false
  end

  create_table "puntuacions", force: :cascade do |t|
    t.integer  "puntaje",                          limit: 2
    t.integer  "id_usuario_que_puntua"
    t.integer  "id_usuario_que_recibe_puntuacion"
    t.integer  "id_perfil_trabajador"
    t.datetime "created_at",                                 null: false
    t.datetime "updated_at",                                 null: false
  end

  create_table "solicitudtrabajos", force: :cascade do |t|
    t.date     "fecha_solicitud"
    t.boolean  "aceptado"
    t.integer  "precio_medio"
    t.integer  "id_trabajo"
    t.integer  "id_usuario"
    t.datetime "created_at",      null: false
    t.datetime "updated_at",      null: false
  end

  create_table "tipotrabajos", force: :cascade do |t|
    t.string   "nombre"
    t.string   "descripcion"
    t.string   "url_foto"
    t.datetime "created_at",  null: false
    t.datetime "updated_at",  null: false
  end

  create_table "trabajos", force: :cascade do |t|
    t.string   "nombre_solicitante"
    t.string   "titulo"
    t.string   "descripcion"
    t.string   "direccion"
    t.string   "url_foto"
    t.string   "ciudad"
    t.date     "fecha_de_requerimiento"
    t.integer  "id_tipo_trabajo"
    t.integer  "id_solicitante"
    t.float    "coordenadax"
    t.float    "coordenaday"
    t.boolean  "activo"
    t.datetime "created_at",             null: false
    t.datetime "updated_at",             null: false
  end

  create_table "users", force: :cascade do |t|
    t.string   "nickname",                     null: false
    t.integer  "telefono",                     null: false
    t.string   "email"
    t.string   "crypted_password"
    t.string   "salt"
    t.string   "nombre"
    t.string   "apellido"
    t.string   "ciudad"
    t.string   "ci"
    t.string   "url_foto"
    t.date     "fecha_nacimiento"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.string   "remember_me_token"
    t.datetime "remember_me_token_expires_at"
  end

  add_index "users", ["nickname"], name: "index_users_on_nickname", unique: true
  add_index "users", ["remember_me_token"], name: "index_users_on_remember_me_token"
  add_index "users", ["telefono"], name: "index_users_on_telefono", unique: true

end
