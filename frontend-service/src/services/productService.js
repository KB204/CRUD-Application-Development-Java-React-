import axios from "axios";

const API_BASE_URL = 'http://localhost:8080';
const PRODUCTS_ENDPOINT = `${API_BASE_URL}/v1/api/products`;

const apiClient = axios.create({
    headers: {
        'Content-Type': 'application/json',
    },
});

export const productService = {
    // Get all categories with optional filtering and pagination
    getAllProducts: async (name = '', page = 0, size = 10, sort = 'name,asc') => {
        try {
            const response = await apiClient.get(PRODUCTS_ENDPOINT, {
                params: {
                    name,
                    page,
                    size,
                    sort,
                },
            });
            return response.data;
        } catch (error) {
            console.error('Error fetching categories:', error);
            throw error;
        }
    },

    getProductDetails: async (id) => {
        try {
            const response = await apiClient.get(`${PRODUCTS_ENDPOINT}/${id}`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching product details for ID ${id}:`, error);
            throw error;
        }
    },

    createProduct: async (productData) => {
        try {
            const response = await apiClient.post(PRODUCTS_ENDPOINT, productData);
            return response.data;
        } catch (error) {
            console.error('Error creating product:', error);
            throw error;
        }
    },

    updateProduct: async (id, productData) => {
        try {
            const response = await apiClient.put(`${PRODUCTS_ENDPOINT}/${id}`, productData);
            return response.data;
        } catch (error) {
            console.error(`Error updating product with ID ${id}:`, error);
            throw error;
        }
    },

    deleteProduct: async (id) => {
        try {
            await apiClient.delete(`${PRODUCTS_ENDPOINT}/${id}`);
            return true;
        } catch (error) {
            console.error(`Error deleting product with ID ${id}:`, error);
            throw error;
        }
    },
};
