package com.hikiw.ecommerce.Service;


import com.hikiw.ecommerce.Enum.ErrorCode;
import com.hikiw.ecommerce.Exception.AppException;
import com.hikiw.ecommerce.Mapper.ProductMapper;
import com.hikiw.ecommerce.Model.Request.ProductCreateRequest;
import com.hikiw.ecommerce.Model.Response.ProductResponse;
import com.hikiw.ecommerce.Repository.CategoryRepository;
import com.hikiw.ecommerce.Repository.ProductRepository;
import com.hikiw.ecommerce.Repository.ShopLocationRepository;
import com.hikiw.ecommerce.Repository.ShopRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional(readOnly = true)
public class ProductService {
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ShopRepository shopRepository;
    ShopLocationRepository shopLocationRepository;
    ProductMapper productMapper;



    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request){
        if(productRepository.existsByProductName(request.getProductName())){
            throw new AppException(ErrorCode.PRODUCT_NOT_EXISTED);
        }

        var category = categoryRepository
                .findById(request.getCategoryId())
                .orElseThrow(()-> new AppException(ErrorCode.CATEGORY_NOT_EXISTED));
        var shop = shopRepository
                .findById(request.getShopId())
                .orElseThrow(()-> new AppException(ErrorCode.SHOP_NOT_EXISTED));
        var shopLocation = shopLocationRepository
                .findById(request.getShopLocationId())
                .orElseThrow(()-> new AppException(ErrorCode.SHOP_LOCATION_NOT_EXISTED));

        var product = productMapper.toProductEntity(request);
        product.setCategory(category);
        product.setShop(shop);
        product.setShopLocation(shopLocation);
        var savedProduct = productRepository.save(product);
        return productMapper.toProductResponse(savedProduct);

    }
}
