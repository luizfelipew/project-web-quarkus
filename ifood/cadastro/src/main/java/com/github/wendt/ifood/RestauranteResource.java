package com.github.wendt.ifood;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

@Path("/restaurantes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "restaurante")
public class RestauranteResource {

    @GET
    public List<Restaurante> buscar() {
        return Restaurante.listAll();
    }

    @POST
    @Transactional
    public Response adicionar(Restaurante dto) {
        dto.persist();
        return Response
                .status(Response.Status.CREATED)
                .build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public void atualizar(@PathParam("id") Long id, Restaurante dto) {
        Optional<Restaurante> restaurantOp = Restaurante.findByIdOptional(id);
        if (restaurantOp.isEmpty()){
            throw new NotFoundException();
        }
        Restaurante restaurante = restaurantOp.get();
        restaurante.nome = dto.nome;
        restaurante.persist();
    }

    @DELETE
    @Path("{id}")
    @Transactional
    public void deletar(@PathParam("id") Long id) {
        Optional<Restaurante> restaurantOp = Restaurante.findByIdOptional(id);

        restaurantOp.ifPresentOrElse(Restaurante::delete, () -> {
            throw new NotFoundException();
        });
    }

    // pratos
    @GET
    @Path("/{idRestaurante}/pratos")
    @Tag(name = "prato")
    public List<Prato> buscarPratos(@PathParam("idRestaurante") Long idRestaurante) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
        if (restauranteOp.isEmpty()){
            throw new NotFoundException("Restaurante não existe");
        }
        return Prato.list("restaurante", restauranteOp.get());
    }

    @POST
    @Path("/{idRestaurante}/pratos")
    @Transactional
    @Tag(name = "prato")
    public Response adicionarPratos(@PathParam("idRestaurante") long idRestaurante, Prato dto) {
        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
        if (restauranteOp.isEmpty()){
            throw new NotFoundException("Restaurante não existe");
        }

        // utilizando o dto, recebi detached entity passed to persist
        Prato prato = new Prato();
        prato.nome = dto.nome;
        prato.descricao = dto.descricao;

        prato.preco = dto.preco;
        prato.restaurante = restauranteOp.get();

        prato.persist();

        return Response
                .status(Response.Status.CREATED)
                .build();
    }

    @DELETE
    @Path("/{idRestaurante}/pratos/{id}")
    @Transactional
    @Tag(name = "prato")
    public void deletarPratos(@PathParam("idRestaurante") long idRestaurante,
                              @PathParam("id") Long id) {

        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
        if (restauranteOp.isEmpty()){
            throw new NotFoundException("Restaurante não existe");
        }

        Optional<Prato> pratoOp = Prato.findByIdOptional(id);

        pratoOp.ifPresentOrElse(Prato::delete, () -> {
            throw new NotFoundException();
        });
    }

    @PUT
    @Path("/{idRestaurante}/pratos/{id}")
    @Transactional
    @Tag(name = "prato")
    public void atualizarPratos(@PathParam("idRestaurante") long idRestaurante,
                                @PathParam("id") Long id,
                                Prato dto) {

        Optional<Restaurante> restauranteOp = Restaurante.findByIdOptional(idRestaurante);
        if (restauranteOp.isEmpty()){
            throw new NotFoundException("Restaurante não existe");
        }

        Optional<Prato> pratoOp = Prato.findByIdOptional(id);
        if (pratoOp.isEmpty()){
            throw new NotFoundException("Prato não existe");
        }
        Prato prato = pratoOp.get();
        prato.preco = dto.preco;
        prato.persist();
    }
}