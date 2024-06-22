package cl.itau.challenge.domain.utilities

import cl.itau.challenge.data.model.EarthquakeEntity
import cl.itau.challenge.data.model.EarthquakePropertyEntity
import cl.itau.challenge.data.model.GeometryEntity
import cl.itau.challenge.data.model.UserEntity
import cl.itau.challenge.domain.model.Earthquake
import cl.itau.challenge.domain.model.EarthquakeProperty
import cl.itau.challenge.domain.model.Geometry
import cl.itau.challenge.domain.model.User
import cl.itau.challenge.presentation.model.EarthquakeModel
import cl.itau.challenge.presentation.model.EarthquakePropertyModel
import cl.itau.challenge.presentation.model.GeometryModel

fun EarthquakeEntity.toDomain() = Earthquake(
    id = this.id,
    type = this.type,
    properties = this.properties.toDomain(),
    geometry = geometryEntity.toDomain()
)

fun GeometryEntity.toDomain() = Geometry(
    type = this.type,
    coordinates = this.coordinates
)

fun EarthquakePropertyEntity.toDomain() = EarthquakeProperty(
    mag = this.mag,
    place = this.place,
    time = this.time,
    updated = this.updated,
    tz = this.tz,
    url = this.url,
    detail = this.detail,
    felt = this.felt,
    cdi = this.cdi,
    mmi = this.mmi,
    alert = this.alert,
    status = this.status,
    tsunami = this.tsunami,
    sig = this.sig,
    net = this.net,
    code = this.code,
    ids = this.ids,
    sources = this.sources,
    types = this.types,
    nst = this.nst,
    dmin = this.dmin,
    rms = this.rms,
    gap = this.gap,
    magType = this.magType,
    type = this.type,
    title = this.title
)

fun Earthquake.toModel() = EarthquakeModel(
    id = this.id,
    type = this.type,
    properties = this.properties.toModel(),
    geometryModel = geometry.toModel()
)

fun Geometry.toModel() = GeometryModel(
    type = this.type,
    coordinates = this.coordinates
)

fun EarthquakeProperty.toModel() = EarthquakePropertyModel(
    mag = this.mag,
    place = this.place,
    time = this.time,
    updated = this.updated,
    tz = this.tz,
    url = this.url,
    detail = this.detail,
    felt = this.felt,
    cdi = this.cdi,
    mmi = this.mmi,
    alert = this.alert,
    status = this.status,
    tsunami = this.tsunami,
    sig = this.sig,
    net = this.net,
    code = this.code,
    ids = this.ids,
    sources = this.sources,
    types = this.types,
    nst = this.nst,
    dmin = this.dmin,
    rms = this.rms,
    gap = this.gap,
    magType = this.magType,
    type = this.type,
    title = this.title
)

fun User.toEntity() = UserEntity(
    id = this.id,
    name = this.name,
    lastName = this.lastName,
    email = this.email,
    password = this.password
)