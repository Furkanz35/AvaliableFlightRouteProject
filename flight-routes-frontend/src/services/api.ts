import axios, { AxiosError } from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/route';

export interface Location {
  name: string;
  country: string;
  locationCode: string;
}

export interface Transportation {
  id: number;
  originLocation: string;
  destinationLocation: string;
  transportationType: string;
  operationDays: number[];
}

export interface ValidRoute {
  transportationDtoList: Transportation[];
}

export interface ApiResponse<T> {
  success: boolean;
  message: string;
  data: T;
}

const api = axios.create({
  baseURL: API_BASE_URL,
  timeout: 5000,
});

// Hata yakalama interceptor'ı
api.interceptors.response.use(
  (response) => response,
  (error: AxiosError) => {
    if (error.code === 'ECONNABORTED') {
      return Promise.reject(new Error('Server did not respond. Please try again later.'));
    }
    if (!error.response) {
      return Promise.reject(new Error('Could not connect to server. Please check your internet connection.'));
    }
    return Promise.reject(error);
  }
);

// Location endpoints
export const locationService = {
  getAll: async () => {
    try {
      const response = await api.get<ApiResponse<Location[]>>('/location/get/all');
      return response;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || 'Error loading locations.');
      }
      throw error;
    }
  },
  getByName: async (name: string) => {
    try {
      const response = await api.get<ApiResponse<Location>>(`/location/get/${name}`);
      return response;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || 'Location not found.');
      }
      throw error;
    }
  },
  create: async (location: Location) => {
    try {
      const response = await api.post<ApiResponse<Location>>('/location/save', location);
      return response;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || 'Error saving location.');
      }
      throw error;
    }
  },
  update: async (name: string, location: Location) => {
    try {
      const response = await api.put<ApiResponse<Location>>(`/location/update/${name}`, location);
      return response;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || 'Error updating location.');
      }
      throw error;
    }
  },
  delete: async (name: string) => {
    try {
      const response = await api.delete<ApiResponse<void>>(`/location/delete/${name}`);
      return response;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || 'Error deleting location.');
      }
      throw error;
    }
  },
};

// Transportation endpoints
export const transportationService = {
  getAll: async () => {
    try {
      const response = await api.get<ApiResponse<Transportation[]>>('/transportation/get/all');
      return response;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || 'Error loading transportation.');
      }
      throw error;
    }
  },
  create: async (transportation: Transportation) => {
    try {
      const response = await api.post<ApiResponse<Transportation>>('/transportation/save', transportation);
      return response;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || 'Error saving transportation.');
      }
      throw error;
    }
  },
  update: async (id: number, transportation: Transportation) => {
    try {
      const response = await api.put<ApiResponse<Transportation>>(`/transportation/update/${id}`, transportation);
      return response;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || 'Error updating transportation.');
      }
      throw error;
    }
  },
  delete: async (id: number) => {
    try {
      const response = await api.delete<ApiResponse<void>>(`/transportation/delete/${id}`);
      return response;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || 'Error deleting transportation.');
      }
      throw error;
    }
  }
};

// Valid routes endpoint
export const validRouteService = {
  findValidRoutes: async (originLocationName: string, destinationLocationName: string, date: string) => {
    try {
      const response = await api.get<ApiResponse<ValidRoute[]>>('/get', {
        params: {
          originLocationName,
          destinationLocationName,
          date,
        },
      });
      return response;
    } catch (error) {
      if (axios.isAxiosError(error)) {
        throw new Error(error.response?.data?.message || 'Error searching for routes.');
      }
      throw error;
    }
  },
}; 