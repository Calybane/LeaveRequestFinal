
export interface LeaveRequestView {
  id: number;
  login: string;
  daysLeft: number;
  leaveFrom: Date;
  leaveTo: Date;
  daysTaken: number;
  requestDate: Date;
  approvalDate: Date;
  status: string;
}
