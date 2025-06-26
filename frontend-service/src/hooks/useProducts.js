import { useState, useEffect, useCallback } from 'react';
import {productService} from "../services/productService.js";

export const useProducts = (initialName = '', initialPage = 0, initialSize = 10) => {
    const [products, setProducts] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [pagination, setPagination] = useState({
        page: initialPage,
        size: initialSize,
        totalElements: 0,
        totalPages: 0,
    });
    const [filterName, setFilterName] = useState(initialName);

    const fetchProducts = useCallback(async (name = filterName, page = pagination.page, size = pagination.size) => {
        setLoading(true);
        setError(null);
        try {
            const response = await productService.getAllProducts(name, page, size);
            setProducts(response.content);
            setPagination({
                page: response.number,
                size: response.size,
                totalElements: response.totalElements,
                totalPages: response.totalPages,
            });
        } catch (err) {
            setError(err.message || 'Failed to fetch products');
        } finally {
            setLoading(false);
        }
    }, [filterName, pagination.page, pagination.size]);

    useEffect(() => {
        fetchProducts();
    }, [fetchProducts]);

    const handlePageChange = (newPage) => {
        setPagination((prev) => ({ ...prev, page: newPage }));
    };

    const handleFilterChange = (name) => {
        setFilterName(name);
        setPagination((prev) => ({ ...prev, page: 0 }));
    };

    return {
        products,
        loading,
        error,
        pagination,
        filterName,
        handlePageChange,
        handleFilterChange,
        refreshProducts: fetchProducts,
    };
};

export const useProductDetails = (productId) => {
    const [product, setProduct] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const fetchProductDetails = useCallback(async () => {
        if (!productId) return;

        setLoading(true);
        setError(null);
        try {
            const data = await productService.getProductDetails(productId);
            setProduct(data);
        } catch (err) {
            setError(err.message || 'Failed to fetch product details');
        } finally {
            setLoading(false);
        }
    }, [productId]);

    useEffect(() => {
        fetchProductDetails();
    }, [fetchProductDetails]);

    return {
        product,
        loading,
        error,
        refreshproduct: fetchProductDetails,
    };
};

export const useProductMutations = () => {
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(false);

    const resetState = () => {
        setError(null);
        setSuccess(false);
    };

    const createNewProduct = async (productData) => {
        resetState();
        setLoading(true);
        try {
            const result = await productService.createProduct(productData);
            setSuccess(true);
            return result;
        } catch (err) {
            setError(err.message || 'Failed to create product');
            throw err;
        } finally {
            setLoading(false);
        }
    };

    const editProduct = async (id, productData) => {
        resetState();
        setLoading(true);
        try {
            const result = await productService.updateProduct(id, productData);
            setSuccess(true);
            return result;
        } catch (err) {
            setError(err.message || 'Failed to update product');
            throw err;
        } finally {
            setLoading(false);
        }
    };

    const removeProduct = async (id) => {
        resetState();
        setLoading(true);
        try {
            await productService.deleteProduct(id);
            setSuccess(true);
            return true;
        } catch (err) {
            setError(err.message || 'Failed to delete product');
            throw err;
        } finally {
            setLoading(false);
        }
    };

    return {
        createNewProduct,
        editProduct,
        removeProduct,
        loading,
        error,
        success,
        resetState,
    };
};