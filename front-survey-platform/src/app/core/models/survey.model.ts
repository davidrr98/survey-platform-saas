import { SurveyStatus } from './enums/survey-status.enum';
import { Question } from './question.model';

export interface Survey {
  id?: string;
  title: string;
  description: string;
  status: SurveyStatus;
  createdAt?: string;
  updatedAt?: string;
  questions?: Question[];
}

export interface SurveyFormData {
  title: string;
  description: string;
  status: SurveyStatus;
}