package org.acme.controller;

import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import org.acme.entity.Product;
import org.acme.service.ProductService;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.List;

@Path("/products")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductController {

    @Inject
    ProductService productService;
    @Inject
    JsonWebToken jwt;

//    @GET
//
//    public Response getAllProducts() {
//        List<Product> products = productService.getAllProducts();
//        return Response.ok(products).build();
//    }
@GET
@RolesAllowed("user")
public Response getAllProducts(@Context SecurityContext securityContext) {
    if (isUserLoggedIn(securityContext)) {
        List<Product> products = productService.getAllProducts();
        return Response.ok(products).build();
    } else {
        return Response.status(Response.Status.UNAUTHORIZED).build();
    }
}

    @GET
  @RolesAllowed("user")
    @Path("/{id}")
    public Response getProductById(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        if (isUserLoggedIn(securityContext)) {
            Product product = productService.getProductById(id);
            if (product != null) {
                return Response.ok(product).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @GET
 @RolesAllowed("user")
    @Path("/search")
    public Response searchProductsByName(@QueryParam("name") String name, @Context SecurityContext securityContext) {
        if (isUserLoggedIn(securityContext)) {
            List<Product> products = productService.searchProductsByName(name);
            return Response.ok(products).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @POST
    @Path("/add")
  @RolesAllowed("user")
    public Response addProduct(@Valid Product product, @Context SecurityContext securityContext) {
        if (isUserLoggedIn(securityContext)) {
            Product newProduct = productService.addProduct(product);
            return Response.status(Response.Status.CREATED).entity(newProduct).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @PUT
    @RolesAllowed("user")
    @Path("/{id}")
    public Response updateProduct(@PathParam("id") Long id, @Valid Product product, @Context SecurityContext securityContext) {
        if (isUserLoggedIn(securityContext)) {
            Product updatedProduct = productService.updateProduct(id, product);
            if (updatedProduct != null) {
                return Response.ok(updatedProduct).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    @DELETE
  @RolesAllowed("user")
    @Path("/{id}")
    public Response deleteProduct(@PathParam("id") Long id, @Context SecurityContext securityContext) {
        if (isUserLoggedIn(securityContext)) {
            boolean deleted = productService.deleteProduct(id);
            if (deleted) {
                return Response.noContent().build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } else {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
    }

    private boolean isUserLoggedIn(SecurityContext securityContext) {
        return securityContext.isUserInRole("user") && jwt.getClaim("sub") != null;
    }
}
