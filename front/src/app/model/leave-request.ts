
export interface LeaveRequest {
  id: number;
  login: string;
  typeAbsence: string;
  leaveFrom: Date;
  leaveTo: Date;
  daysTaken: number;
  requestDate: Date;
  approvalDate: Date;
  status: string;
  description: string;
}
