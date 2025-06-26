import axios from "axios";

const API_BASE_URL = 'http://localhost:8080';
const CATEGORIES_ENDPOINT = `${API_BASE_URL}/v1/api/categories`;

const apiClient = axios.create({
    headers: {
        'Content-Type': 'application/json',
    },
});

export const categoryService = {
    // Get all categories with optional filtering and pagination
    getAllCategories: async (name = '', page = 0, size = 10, sort = 'name,asc') => {
        try {
            const response = await apiClient.get(CATEGORIES_ENDPOINT, {
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

    getCategoryDetails: async (id) => {
        try {
            const response = await apiClient.get(`${CATEGORIES_ENDPOINT}/${id}/details`);
            return response.data;
        } catch (error) {
            console.error(`Error fetching category details for ID ${id}:`, error);
            throw error;
        }
    },

    createCategory: async (categoryData) => {
        try {
            const response = await apiClient.post(CATEGORIES_ENDPOINT, categoryData);
            return response.data;
        } catch (error) {
            console.error('Error creating category:', error);
            throw error;
        }
    },

    updateCategory: async (id, categoryData) => {
        try {
            const response = await apiClient.put(`${CATEGORIES_ENDPOINT}/${id}`, categoryData);
            return response.data;
        } catch (error) {
            console.error(`Error updating category with ID ${id}:`, error);
            throw error;
        }
    },

    deleteCategory: async (id) => {
        try {
            await apiClient.delete(`${CATEGORIES_ENDPOINT}/${id}`);
            return true;
        } catch (error) {
            console.error(`Error deleting category with ID ${id}:`, error);
            throw error;
        }
    },
};
