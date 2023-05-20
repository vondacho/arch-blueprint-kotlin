package edu.obya.blueprint.client.domain.service

import edu.obya.blueprint.client.domain.model.ClientId

class ClientNotFoundException(id: ClientId): Throwable("Client $id does not exist")
class ClientAlreadyExistsException(name: String): Throwable("Client with name $name already exists")
