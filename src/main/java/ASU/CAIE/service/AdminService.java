package ASU.CAIE.service;

import ASU.CAIE.config.AppConfig;

public class AdminService {
    
    public int getTotalStudents() {
        if (AppConfig.USE_MOCK_DATA) {
            return MockDataProvider.getMockStudents().size();
        }
        return 0; // Real DB logic would go here
    }
    
    public int getTotalInstructors() {
        if (AppConfig.USE_MOCK_DATA) {
            return MockDataProvider.getMockInstructors().size();
        }
        return 0; // Real DB logic would go here
    }
    
    public int getTotalCourses() {
        if (AppConfig.USE_MOCK_DATA) {
            return MockDataProvider.getMockCourses().size();
        }
        return 0; // Real DB logic would go here
    }
    
    public int getActiveEnrollments() {
        if (AppConfig.USE_MOCK_DATA) {
            return 12; // Mock count
        }
        return 0; // Real DB logic would go here
    }
}
