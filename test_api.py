import requests
import json

BASE_URL = "http://localhost:8080"

# Colors for terminal output
GREEN = "\033[92m"
RED = "\033[91m"
RESET = "\033[0m"

def print_status(test_name, response):
    if response.status_code in [200, 201]:
        print(f"{GREEN}✅ [PASS] {test_name}{RESET}")
        return response.json() if response.text else {}
    else:
        print(f"{RED}❌ [FAIL] {test_name} - Status: {response.status_code}{RESET}")
        print(f"   Response: {response.text}")
        return None

def run_tests():
    print("🚀 Starting Comprehensive API Tests...\n")
    
    # Store IDs for linking later
    data = {
        "dept_id": None,
        "emp_worker_id": None,
        "emp_manager_id": None,
        "client_id": None,
        "project_id": None,
        "eval_id": None
    }

    # ==========================================
    # 1. DEPARTMENT MODULE
    # ==========================================
    print("--- 1. Testing Department ---")
    dept_payload = {
        "departmentName": "Python Auto-Test Dept",
        "departmentLocation": "Cloud Server",
        "departmentCapacity": 50
    }
    res = requests.post(f"{BASE_URL}/departments", json=dept_payload)
    dept_data = print_status("Create Department", res)
    if dept_data: data["dept_id"] = dept_data.get('departmentId')

    # ==========================================
    # 2. EMPLOYEE MODULE (Create 2 Employees)
    # ==========================================
    print("\n--- 2. Testing Employee ---")
    
    # Employee A: The Worker
    worker_payload = {
        "employeeName": "Test Worker (Alice)", 
        "salary": {"baseSalary": 50000.0}
    }
    res = requests.post(f"{BASE_URL}/employees", json=worker_payload)
    worker_data = print_status("Create Worker Employee", res)
    if worker_data: data["emp_worker_id"] = worker_data.get('employeeId')

    # Employee B: The Manager
    manager_payload = {
        "employeeName": "Test Manager (Bob)",
        "salary": {"baseSalary": 90000.0}
    }
    res = requests.post(f"{BASE_URL}/employees", json=manager_payload)
    manager_data = print_status("Create Manager Employee", res)
    if manager_data: data["emp_manager_id"] = manager_data.get('employeeId')

    if not data["emp_worker_id"] or not data["emp_manager_id"]:
        print(f"{RED}⚠️ Critical: Employee creation failed. Stopping tests.{RESET}")
        return

    # ==========================================
    # 3. WORK ALLOCATION (Client -> Project -> Responsibility)
    # ==========================================
    print("\n--- 3. Testing Work Allocation ---")
    
    # A. Create Client
    # (If you haven't made ClientController yet, this might fail (404). 
    # Logic will try to use ID 1 from SQL script as fallback)
    client_payload = {
        "clientName": "Automated Client Corp",
        "organization": "Robots Inc",
        "budget": 100000.00,
        "clientType": "Enterprise",
        "status": "Active"
    }
    res = requests.post(f"{BASE_URL}/clients", json=client_payload)
    if res.status_code == 404:
        print(f"{RED}⚠️ Client Controller not found (404). Using ID 1 from Seed Data.{RESET}")
        data["client_id"] = 1
    else:
        client_data = print_status("Create Client", res)
        if client_data: data["client_id"] = client_data.get('clientId')

    # B. Create Project
    if data["client_id"]:
        proj_payload = {
            "projectName": "Python API Integration",
            "projectDuration": "3 Months",
            "client": {"clientId": data["client_id"]},
            "department": {"departmentId": data["dept_id"]},
            "projectLead": {"employeeId": data["emp_manager_id"]} # Bob leads
        }
        res = requests.post(f"{BASE_URL}/projects", json=proj_payload)
        proj_data = print_status("Create Project", res)
        if proj_data: data["project_id"] = proj_data.get('projectId')

# C. Assign Responsibility
    if data["project_id"]:
        resp_payload = {
            # Change "DEVELOPMENT" to "CONTRIBUTOR" (or PROJECT_LEAD, etc.)
            "responsibilityType": "CONTRIBUTOR", 
            
            # Ensure this matches your ClearanceLevel enum too. 
            # If your code uses different levels, change this as well.
            "clearanceLevel": "INTERNAL",        
            
            "startDate": "2025-02-01",
            "project": {"projectId": data["project_id"]},
            "employee": {"employeeId": data["emp_worker_id"]} 
        }
        res = requests.post(f"{BASE_URL}/responsibilities", json=resp_payload)
        print_status("Create Responsibility", res)

    # ==========================================
    # 4. PERFORMANCE MODULE
    # ==========================================
    print("\n--- 4. Testing Performance ---")

    # A. Evaluation (Master Record)
    eval_payload = {
        "employeeId": data["emp_worker_id"],
        "evaluationPeriod": "2025 Q1",
        "performanceRating": "Pending",
        "comments": "Initial automated review"
    }
    res = requests.post(f"{BASE_URL}/performance_evaluations", json=eval_payload)
    eval_data = print_status("Create Evaluation", res)
    if eval_data: data["eval_id"] = eval_data.get('evaluationId')

    # B. Feedback (Bob reviews Alice)
    feedback_payload = {
        "employee": {"employeeId": data["emp_worker_id"]},
        "reviewer": {"employeeId": data["emp_manager_id"]},
        "rating": 4.5,
        "comments": "Good coding speed on the Python script.",
        "feedbackDate": "2025-02-15",
        "evaluationId": data["eval_id"]
    }
    res = requests.post(f"{BASE_URL}/performance-feedback", json=feedback_payload)
    print_status("Create Feedback", res)

    # ==========================================
    # 5. VERIFICATION & CLEANUP
    # ==========================================
    print("\n--- 5. Verification & Cleanup ---")
    
    # Get Employee Details (Check if Salary links correctly without recursion)
    res = requests.get(f"{BASE_URL}/employees/{data['emp_worker_id']}")
    print_status(f"Get Employee {data['emp_worker_id']} Details", res)

    # Delete the Worker (Should cascade delete their Salary/Feedback etc)
    # Uncomment below to test deletion
    # res = requests.delete(f"{BASE_URL}/employees/{data['emp_worker_id']}")
    # if res.status_code == 204:
    #     print(f"{GREEN}✅ [PASS] Delete Employee{RESET}")
    # else:
    #     print_status("Delete Employee", res)

    print("\n🏁 All Tests Completed.")

if __name__ == "__main__":
    try:
        run_tests()
    except requests.exceptions.ConnectionError:
        print(f"{RED}❌ Error: Could not connect to the server. Is Spring Boot running?{RESET}")